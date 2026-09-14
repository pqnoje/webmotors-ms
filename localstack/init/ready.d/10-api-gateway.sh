#!/bin/bash
set -euo pipefail

api_id="$(awslocal apigateway get-rest-apis \
  --query 'items[?name==`webmotors-local`].id | [0]' \
  --output text 2>/dev/null || true)"

if [[ -n "$api_id" && "$api_id" != "None" ]]; then
  echo "Webmotors Local API Gateway ja existe: http://localhost:4566/restapis/$api_id/local/_user_request_"
  exit 0
fi

api_id="$(awslocal apigateway create-rest-api \
  --name webmotors-local \
  --endpoint-configuration types=REGIONAL \
  --query id \
  --output text)"

root_id="$(awslocal apigateway get-resources \
  --rest-api-id "$api_id" \
  --query 'items[?path==`/`].id' \
  --output text)"

create_route_group() {
  local path_part="$1"
  local target_url="$2"

  local resource_id
  resource_id="$(awslocal apigateway create-resource \
    --rest-api-id "$api_id" \
    --parent-id "$root_id" \
    --path-part "$path_part" \
    --query id \
    --output text)"

  awslocal apigateway put-method \
    --rest-api-id "$api_id" \
    --resource-id "$resource_id" \
    --http-method ANY \
    --authorization-type NONE >/dev/null

  awslocal apigateway put-integration \
    --rest-api-id "$api_id" \
    --resource-id "$resource_id" \
    --http-method ANY \
    --type HTTP_PROXY \
    --integration-http-method ANY \
    --uri "$target_url" >/dev/null

  local proxy_id
  proxy_id="$(awslocal apigateway create-resource \
    --rest-api-id "$api_id" \
    --parent-id "$resource_id" \
    --path-part '{proxy+}' \
    --query id \
    --output text)"

  awslocal apigateway put-method \
    --rest-api-id "$api_id" \
    --resource-id "$proxy_id" \
    --http-method ANY \
    --authorization-type NONE \
    --request-parameters method.request.path.proxy=true >/dev/null

  awslocal apigateway put-integration \
    --rest-api-id "$api_id" \
    --resource-id "$proxy_id" \
    --http-method ANY \
    --type HTTP_PROXY \
    --integration-http-method ANY \
    --uri "$target_url/{proxy}" \
    --request-parameters integration.request.path.proxy=method.request.path.proxy >/dev/null
}

create_route_group 'usuarios' 'http://usuarios-service:8081/usuarios'
create_route_group 'anuncios' 'http://anuncios-service:8082/anuncios'

awslocal apigateway create-deployment \
  --rest-api-id "$api_id" \
  --stage-name local >/dev/null

echo "Webmotors Local API Gateway criado: http://localhost:4566/restapis/$api_id/local/_user_request_"

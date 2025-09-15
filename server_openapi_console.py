import re
import httpx
from fastmcp import FastMCP
# from fastmcp.server.openapi import RouteMap, MCPType

# API Base Url
BASE_URL = "http://127.0.0.1:8080"
# OpenAPI Spec URL
OPENAPI_URL = ""

INCLUDE_PATH_PATTERNS = [r"^/api/sku/.*"]

# Load your OpenAPI spec from a URL
openapi_spec = httpx.get(
    OPENAPI_URL,
    timeout=httpx.Timeout(None, read=240.0),
).json()


# 只把部分 path 傳給 FastMCP
def filter_openapi_paths(spec: dict, patterns: list[str]) -> dict:
    compiled = [re.compile(p) for p in patterns]
    return {
        **spec,
        "paths": {
            p: obj
            for p, obj in spec.get("paths", {}).items()
            if any(rx.search(p) for rx in compiled)
        },
    }


openapi_spec = filter_openapi_paths(openapi_spec, INCLUDE_PATH_PATTERNS)

# Create an HTTP client for the target API
client = httpx.AsyncClient(base_url=BASE_URL)

mcp = FastMCP.from_openapi(
    openapi_spec=openapi_spec,
    client=client,
    # route_maps=[
    #     # Include routes with pattern /api/sku/* as tools
    #     RouteMap(
    #         pattern=r"^/api/sku/.*",
    #         mcp_type=MCPType.TOOL,
    #     ),
    #     # Exclude all other routes
    #     RouteMap(
    #         pattern=r".*",
    #         mcp_type=MCPType.EXCLUDE,
    #     ),
    # ],
)

if __name__ == "__main__":
    # mcp.run(transport="http")
    print(filter_openapi_paths(openapi_spec, INCLUDE_PATH_PATTERNS))

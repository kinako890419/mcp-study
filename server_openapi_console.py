import re
import httpx
from fastmcp import FastMCP
# from fastmcp.server.openapi import RouteMap, MCPType

# API Base Url (建立 request 的 base url)
BASE_URL = "http://127.0.0.1:8080"
# OpenAPI Spec URL
OPENAPI_URL = ""

INCLUDE_PATH_PATTERNS = [r"^/api/sku/.*"]

# 1. Load your OpenAPI spec from a URL
openapi_spec = httpx.get(
    OPENAPI_URL,
    timeout=httpx.Timeout(None, read=240.0),
).json()


# 只擷取 doc 的一部分
def filter_openapi_paths(spec: dict, patterns: list[str]) -> dict:
    compiled = [re.compile(p) for p in patterns]
    # 回傳一份新的 spec，但只保留 paths 中符合正則表達式的部分
    return {
        **spec,
        "paths": {
            p: obj
            for p, obj in spec.get("paths", {}).items()
            if any(rx.search(p) for rx in compiled)
        },
    }


openapi_spec = filter_openapi_paths(openapi_spec, INCLUDE_PATH_PATTERNS)

# 2. Create an HTTP client for the target API
# 實際要發送 request 的地方
client = httpx.AsyncClient(base_url=BASE_URL)

# Create the MCP server from the OpenAPI spec
mcp = FastMCP.from_openapi(
    openapi_spec=openapi_spec,  # OpenAPI schema as a dictionary
    client=client,  # httpx AsyncClient for making HTTP requests
)

if __name__ == "__main__":
    mcp.run(transport="http")
    # print(filter_openapi_paths(openapi_spec, INCLUDE_PATH_PATTERNS))
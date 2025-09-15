import httpx
from fastmcp import FastMCP
from fastmcp.server.openapi import RouteMap, MCPType

from model import WeatherData

"""Open API"""

BASE_URL = "http://127.0.0.1:8888"
OPENAPI_URL = "http://localhost:8888/api-docs"

# Create an HTTP client for the target API
client = httpx.AsyncClient(
    base_url=BASE_URL
)

# Get API doc
openapi_spec = httpx.get(OPENAPI_URL).json()

# Create the MCP server from the OpenAPI spec
mcp = FastMCP.from_openapi(
    openapi_spec=openapi_spec,
    client=client,
    route_maps=[

        # Exclude register and login endpoints
        RouteMap(
            pattern=r"^/auth/.*",
            mcp_type=MCPType.EXCLUDE,
        ),

        # # GET requests with path parameters become ResourceTemplates
        # 拿 resources 的時候要輸參數
        # RouteMap(
        #     methods=["GET"],
        #     pattern=r".*\{.*\}.*",
        #     mcp_type=MCPType.RESOURCE_TEMPLATE
        # ),

        # # All other GET requests become Resources
        # RouteMap(
        #     methods=["GET"],
        #     pattern=r".*",
        #     mcp_type=MCPType.RESOURCE
        # ),

        # # 用 API doc 的 tags 做篩選
        # # Exclude all routes tagged "internal"
        # RouteMap(
        #     tags={"internal"},
        #     mcp_type=MCPType.EXCLUDE,
        # ),
    ],
)

""" Add custom tool"""


@mcp.tool()
def get_weather(city: str, unit: str = "celsius") -> WeatherData:
    """Get weather for a city - returns structured data."""
    # Simulated weather data
    return WeatherData(
        city=city,
        temperature=22.5,
        unit=unit,
    )


if __name__ == "__main__":
    mcp.run(transport="http")

    # # Option 2: With ASGI app
    # app = mcp.http_app()
    # # To run your ASGI application
    # import uvicorn
    #
    # uvicorn.run(app, port=8000)

import httpx
from fastmcp import FastMCP
# from fastmcp.server.openapi import RouteMap, MCPType
from fastmcp.experimental.server.openapi import RouteMap, MCPType

from pydantic import BaseModel, Field

mcp = FastMCP(name="202509 open api ==> mcp server")


class WeatherData(BaseModel):
    """Weather information structure."""

    temperature: float = Field(description="Temperature in Celsius")
    humidity: float = Field(description="Humidity percentage")
    condition: str
    wind_speed: float


@mcp.tool()
def get_weather() -> WeatherData:
    """Get weather for a city - returns structured data."""
    # Simulated weather data
    return WeatherData(
        temperature=22.5,
        humidity=45.0,
        condition="sunny",
        wind_speed=5.2,
    )


"""Open API"""

# Create an HTTP client for the target API
client = httpx.AsyncClient(
    base_url="http://localhost:8080"
)

# Get API doc from http://localhost:8080/api-docs
openapi_url = "http://localhost:8080/api-docs"
openapi_spec = httpx.get(openapi_url).json()

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

# Add custom tool
mcp.add_tool(get_weather)

if __name__ == "__main__":
    # mcp.run(transport="http")

    # Option 2: With ASGI app
    app = mcp.http_app()
    # To run your ASGI application
    import uvicorn

    uvicorn.run(app, port=8000)

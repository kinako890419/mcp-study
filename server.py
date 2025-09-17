from mcp.server.fastmcp import FastMCP
from model import WeatherData, GetWeatherData, GetWeatherDataDetail

mcp = FastMCP("weather http server example", port=8001)


@mcp.tool()
def get_weather(city: str, unit: str = "celsius") -> WeatherData:
    """Get weather for a city - returns structured data."""
    return WeatherData(
        city=city,
        temperature=22.5,
        unit=unit,
    )


if __name__ == "__main__":
    mcp.run(transport="streamable-http")

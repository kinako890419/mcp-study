from mcp.server.fastmcp import FastMCP
from model import WeatherData, GetWeatherData, GetWeatherDataDetail

mcp = FastMCP("streamable-http example", port=8001)


@mcp.tool()
def get_weather(city: str, unit: str = "celsius") -> WeatherData:
    """Get weather for a city - returns structured data."""
    return WeatherData(
        city=city,
        temperature=22.5,
        unit=unit,
    )


@mcp.tool()
def get_weather_req_body(data: GetWeatherData) -> WeatherData:
    """Get weather for a city - returns structured data."""
    data = WeatherData()
    data.city = data.city
    data.temperature = 50
    data.unit = data.unit
    details = GetWeatherDataDetail()
    details.location = "Some Location"
    details.timezone = "Some Timezone"
    data.details = details

    return data


@mcp.tool()
def get_weather_no_param() -> WeatherData:
    """Get weather for a city - returns structured data."""
    # Simulated weather data
    return WeatherData(
        city="San Francisco",
        temperature=20.5,
        unit="celsius"
    )


@mcp.tool()
def get_weather_no_response():
    pass


if __name__ == "__main__":
    mcp.run(transport="streamable-http")

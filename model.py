from pydantic import BaseModel, Field


class GetWeatherDataDetail(BaseModel):
    """ Input structure for getting weather information."""

    location: str = Field(description="Location name")
    timezone: str = Field(description="Timezone name")

class WeatherData(BaseModel):
    """Weather information structure."""

    city: str = Field(description="City name")
    temperature: float = Field(description="Temperature in Celsius")
    unit: str = Field(description="Unit of temperature")
    details: GetWeatherDataDetail | None = Field(default=None,
                                                 description="Additional weather details")

class GetWeatherData(BaseModel):
    """ Input structure for getting weather information."""

    city: str = Field(description="City name")
    unit: str = Field(description="Unit of temperature")

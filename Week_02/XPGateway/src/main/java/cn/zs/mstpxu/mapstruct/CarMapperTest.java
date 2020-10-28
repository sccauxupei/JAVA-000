package cn.zs.mstpxu.mapstruct;


import org.junit.Test;


public class CarMapperTest {
	
	@Test
	public void shouldMapCarToDto() {
	    //given
	    Car car = new Car( "Morris", 5, CarType.SEDAN );
	    //
	 
	    //when
	    CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
	 
	    //then
//	    assertThat( carDto ).isNotNull();
//	    assertThat( carDto.getMake() ).isEqualTo( "Morris" );
//	    assertThat( carDto.getSeatCount() ).isEqualTo( 5 );
//	    assertThat( carDto.getType() ).isEqualTo( "SEDAN" );
	    System.out.println(carDto);
	}
	
}

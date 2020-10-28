package cn.zs.mstpxu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cn.zs.mstpxu.mapstruct.Car;
import cn.zs.mstpxu.mapstruct.CarDto;
import cn.zs.mstpxu.mapstruct.CarMapper;
import cn.zs.mstpxu.mapstruct.CarType;


public class CarMapperTest {
	
	@Test
	public void shouldMapCarToDto() {
	    //given
	    Car car = new Car( "Morris", 5, CarType.SEDAN );
	 
	    //when
	    CarDto carDto = CarMapper.INSTANCE.carToCarDto(car);
	 
	    //then
	    assertThat( carDto ).isNotNull();
	    assertThat( carDto.getMake() ).isEqualTo( "Morris" );
	    assertThat( carDto.getSeatCount() ).isEqualTo( 5 );
	    assertThat( carDto.getType() ).isEqualTo( "SEDAN" );
	}
	
}


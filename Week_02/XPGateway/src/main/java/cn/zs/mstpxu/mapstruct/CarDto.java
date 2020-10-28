package cn.zs.mstpxu.mapstruct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class CarDto {
    private String make;
    private int seatCount;
    private String type;
    private int added;
}

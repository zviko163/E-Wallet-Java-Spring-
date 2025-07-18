package co.zw.logarithm.coverlinkonboarding.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiValidationError implements ApiSubError {
    private Object object;
    private String field;
    private Object rejectedValue;
    private String message;



}

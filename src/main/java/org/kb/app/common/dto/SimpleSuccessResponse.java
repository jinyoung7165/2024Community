package org.kb.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class SimpleSuccessResponse {

    @NotNull
    private Long id;

}

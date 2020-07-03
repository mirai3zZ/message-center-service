package com.yangyang.cloud.message.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * @author daiyan
 */
@Data
public class MessageTypeAddDTO {
	@NotBlank
	@Length(max=20)
	@Pattern(regexp="^[1-9][0-9]*$")
	private String mainTypeId;
	
	@Length(max=20)
	private String mainTypeName;
	
	@NotBlank
	@Length(max=20)
	private String typeName;

	@NotBlank
	@Length(max=20)
	private String typeCode;
	
	@NotBlank
	@Pattern(regexp="^(Y|N)$")
	private String defaultMailSetting;
	
	@NotBlank
	@Pattern(regexp="^(Y|N)$")
	private String defaultEmailSetting;
	
	@NotBlank
	@Pattern(regexp="^(Y|N)$")
	private String defaultSmsSetting;
	
	@NotBlank
	@Pattern(regexp="^(Y|N)$")
	private String canEdit;
	
	@NotBlank
	@Pattern(regexp="^(inner|customer)$")
	private String usingObjectType;
}

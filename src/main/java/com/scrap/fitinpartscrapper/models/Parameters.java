package com.scrap.fitinpartscrapper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameters {

	private String brandId;
	private String modelId;
	private String bodyId;
	private String engineId;
	private String engineTypeId;
	private String categoryId;
	private String subcategoryId;
}

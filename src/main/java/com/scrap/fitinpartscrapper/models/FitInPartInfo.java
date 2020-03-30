package com.scrap.fitinpartscrapper.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitInPartInfo {

	private String brandName;
	private String modelName;
	private String bodyName;
	private String engineName;
	private String engineTypeName;
	private String categoryName;
	private String subCategoryName;
	private String productURL;
}

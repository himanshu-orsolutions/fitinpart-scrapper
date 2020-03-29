package com.scrap.fitinpartscrapper.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fitinpart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitInPart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "parent_id")
	private Long parentId;

	@NotNull
	@Column(name = "info")
	private String info;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tag")
	private FitInPartTag tag;

	@Column(name = "next_url")
	private String nextURL;

	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "http_body")
	private String httpBody;

	@Column(name = "is_scrapped")
	private Boolean isScrapped;
}

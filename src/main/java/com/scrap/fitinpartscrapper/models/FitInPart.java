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

	@NotNull
	@Column(name = "info")
	private String info;

	@NotNull
	@Column(name = "parameters")
	private String parameters;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tag")
	private FitInPartTag tag;

	@Column(name = "is_scrapped")
	private Boolean isScrapped;
}

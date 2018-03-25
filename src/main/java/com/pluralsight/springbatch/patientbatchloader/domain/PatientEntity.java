package com.pluralsight.springbatch.patientbatchloader.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "patient")
public class PatientEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "patient_id")
	private Long id;

	@NotNull
	@Column(name = "source_id", nullable = false)
	private String sourceId;

	@NotNull
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_initial", nullable = true)
	private String middleInitial;

	@NotNull
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email_address", nullable = true)
	private String emailAddress;

	@NotNull
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@NotNull
	@Column(name = "street", nullable = false)
	private String street;

	@NotNull
	@Column(name = "city", nullable = false)
	private String city;

	@NotNull
	@Column(name = "state", nullable = false)
	private String state;

	@NotNull
	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@NotNull
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	@NotNull
	@Column(name = "social_security_number", nullable = false)
	private String socialSecurityNumber;

	public PatientEntity() {

	}

	public PatientEntity(@NotNull String sourceId, @NotNull String firstName, String middleInitial,
			@NotNull String lastName, String emailAddress, @NotNull String phoneNumber, @NotNull String street,
			@NotNull String city, @NotNull String state, @NotNull String zipCode, @NotNull LocalDate birthDate,
			@NotNull String socialSecurityNumber) {
		super();
		this.sourceId = sourceId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.birthDate = birthDate;
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public PatientEntity(Long id, @NotNull String sourceId, @NotNull String firstName, String middleInitial,
			@NotNull String lastName, String emailAddress, @NotNull String phoneNumber, @NotNull String street,
			@NotNull String city, @NotNull String state, @NotNull String zipCode, @NotNull LocalDate birthDate,
			@NotNull String socialSecurityNumber) {
		super();
		this.id = id;
		this.sourceId = sourceId;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.phoneNumber = phoneNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.birthDate = birthDate;
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the sourceId
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId
	 *            the sourceId to set
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleInitial
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}

	/**
	 * @param middleInitial
	 *            the middleInitial to set
	 */
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress
	 *            the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the birthDate
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the socialSecurityNumber
	 */
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	/**
	 * @param socialSecurityNumber
	 *            the socialSecurityNumber to set
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

    @Override
    public String toString() {
        return "PatientEntity{" +
            "id=" + id +
            ", sourceId='" + sourceId + '\'' +
            ", firstName='" + firstName + '\'' +
            ", middleInitial='" + middleInitial + '\'' +
            ", lastName='" + lastName + '\'' +
            ", emailAddress='" + emailAddress + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", birthDate=" + birthDate +
            ", socialSecurityNumber='" + socialSecurityNumber + '\'' +
            '}';
    }

}

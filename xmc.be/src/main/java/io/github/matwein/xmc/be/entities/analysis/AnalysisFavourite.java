package io.github.matwein.xmc.be.entities.analysis;

import io.github.matwein.xmc.be.entities.PersistentObject;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = AnalysisFavourite.TABLE_NAME)
public class AnalysisFavourite extends PersistentObject {
	public static final String TABLE_NAME = "ANALYSIS_FAVOURITES";
	
	@Column(name = "TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private AnalysisType type;
	
	@Column(name = "TIME_RANGE", nullable = false)
	@Enumerated(EnumType.STRING)
	private TimeRange timeRange;
	
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	
	@Column(name = "START_DATE", nullable = false)
	private LocalDate startDate;
	
	@Column(name = "END_DATE", nullable = false)
	private LocalDate endDate;
	
	@ManyToMany
	@JoinTable(name = TABLE_NAME + "_CASHACCOUNTS",
			joinColumns = { @JoinColumn(name = "ANALYSIS_FAVOURITE_ID") },
			inverseJoinColumns = { @JoinColumn(name = "CASHACCOUNT_ID") })
	private Set<CashAccount> cashAccounts = new HashSet<>();
	
	@ManyToMany
	@JoinTable(name = TABLE_NAME + "_DEPOTS",
			joinColumns = { @JoinColumn(name = "ANALYSIS_FAVOURITE_ID") },
			inverseJoinColumns = { @JoinColumn(name = "DEPOT_ID") })
	private Set<Depot> depots = new HashSet<>();
	
	public AnalysisType getType() {
		return type;
	}
	
	public void setType(AnalysisType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	
	public Set<CashAccount> getCashAccounts() {
		return cashAccounts;
	}
	
	public void setCashAccounts(Set<CashAccount> cashAccounts) {
		this.cashAccounts = cashAccounts;
	}
	
	public Set<Depot> getDepots() {
		return depots;
	}
	
	public void setDepots(Set<Depot> depots) {
		this.depots = depots;
	}
	
	public TimeRange getTimeRange() {
		return timeRange;
	}
	
	public void setTimeRange(TimeRange timeRange) {
		this.timeRange = timeRange;
	}
}

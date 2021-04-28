package org.xmc.be.entities.analysis;

import com.google.common.collect.Sets;
import org.xmc.be.entities.PersistentObject;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.depot.Depot;
import org.xmc.common.stubs.analysis.AnalysisType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = AnalysisFavourite.TABLE_NAME)
public class AnalysisFavourite extends PersistentObject {
	public static final String TABLE_NAME = "ANALYSIS_FAVOURITES";
	
	@Column(name = "TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	private AnalysisType type;
	
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
	private Set<CashAccount> cashAccounts = Sets.newHashSet();
	
	@ManyToMany
	@JoinTable(name = TABLE_NAME + "_DEPOTS",
			joinColumns = { @JoinColumn(name = "ANALYSIS_FAVOURITE_ID") },
			inverseJoinColumns = { @JoinColumn(name = "DEPOT_ID") })
	private Set<Depot> depots = Sets.newHashSet();
	
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
}

/*
 * Copyright 2010-2019 Miyamoto Daisuke.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.insuranceRates;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.RoundingMode;

import vn.com.unit.sparwings.baseunits.intervals.Interval;
import vn.com.unit.sparwings.baseunits.intervals.IntervalMap;
import vn.com.unit.sparwings.baseunits.intervals.LinearIntervalMap;
import vn.com.unit.sparwings.baseunits.money.Money;
import vn.com.unit.sparwings.baseunits.money.MoneyTimeRate;
import vn.com.unit.sparwings.baseunits.money.Proration;
import vn.com.unit.sparwings.baseunits.time.CalendarDate;
import vn.com.unit.sparwings.baseunits.time.CalendarInterval;
import vn.com.unit.sparwings.baseunits.time.Duration;
import vn.com.unit.sparwings.baseunits.util.Ratio;

import org.junit.Test;

/**
 * Example.
 */
public class CalculateRate {
	
	/** 契約日 */
	private static final CalendarDate policyEffectiveDate = CalendarDate.from(2004, 11, 7);
	
	
	/**
	 * Example.
	 */
	
	@Test
	public void testLookUpRate() {
		CalendarDate birthdate = CalendarDate.from(1963, 4, 6);
		Duration ageOnEffectiveDate = birthdate.through(policyEffectiveDate).lengthInMonths();
		Money monthlyPremium = insuranceSchedule().get(ageOnEffectiveDate);
		assertThat(monthlyPremium, is(Money.dollars(150.00)));
	}
	
	/**
	 * 初月の日割り計算の例。
	 */
	@Test
	public void testProrateFirstMonth() {
		// 月額 150.00 USD
		Money monthlyPremium = Money.dollars(150.00);
		
		// 契約月の残り期間
		CalendarInterval entireMonth = policyEffectiveDate.asMonthInterval();
		CalendarInterval remainderOfMonth = policyEffectiveDate.through(entireMonth.end());
		
		// 契約月の残り日数 ÷ 契約月の全日数
		Ratio partOfPayment = Ratio.of(remainderOfMonth.lengthInDaysInt(), entireMonth.lengthInDaysInt());
		
		// 切り捨てで日割り適用
		Money firstPayment = monthlyPremium.applying(partOfPayment, RoundingMode.DOWN);
		assertThat(firstPayment, is(Money.dollars(120.00)));
		
		// Alternative, equivalent calculation
		partOfPayment = remainderOfMonth.length().dividedBy(entireMonth.length());
		firstPayment = Proration.partOfWhole(monthlyPremium, partOfPayment);
		assertThat(firstPayment, is(Money.dollars(120.00)));
	}
	
	/**
	 * Example.
	 */
	@Test
	public void testQuarterlyPremiumPayment() {
		MoneyTimeRate premium = Money.dollars(150.00).per(Duration.months(1));
		Money quarterlyPayment = premium.over(Duration.quarters(1));
		assertThat(quarterlyPayment, is(Money.dollars(450.00)));
	}
	
	private IntervalMap<Duration, Money> insuranceSchedule() {
		Interval<Duration> age25_35 = Interval.over(Duration.years(25), true, Duration.years(35), false);
		Interval<Duration> age35_45 = Interval.over(Duration.years(35), true, Duration.years(45), false);
		Interval<Duration> age45_55 = Interval.over(Duration.years(45), true, Duration.years(55), false);
		Interval<Duration> age55_65 = Interval.over(Duration.years(55), true, Duration.years(65), false);
		
		IntervalMap<Duration, Money> schedule = new LinearIntervalMap<Duration, Money>();
		schedule.put(age25_35, Money.dollars(100.00));
		schedule.put(age35_45, Money.dollars(150.00));
		schedule.put(age45_55, Money.dollars(200.00));
		schedule.put(age55_65, Money.dollars(250.00));
		return schedule;
	}
	
}

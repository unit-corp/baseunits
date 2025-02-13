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
package vn.com.unit.sparwings.baseunits.time.formatter;

import java.util.Locale;

import vn.com.unit.sparwings.baseunits.time.TimePoint;
import vn.com.unit.sparwings.baseunits.timeutil.Clock;

/**
 * {@link RelativeTimePointFormatter}の骨格実装クラス。
 * 
 * @author daisuke
 * @since 2.0
 */
public abstract class AbstractRelativeTimePointFormatter implements RelativeTimePointFormatter {
	
	@Override
	public String format(TimePoint target) {
		return format(target, Clock.now(), Locale.getDefault());
	}
	
	@Override
	public String format(TimePoint target, Locale locale) {
		return format(target, Clock.now(), locale);
	}
	
	@Override
	public String format(TimePoint target, TimePoint standard) {
		return format(target, standard, Locale.getDefault());
	}
}

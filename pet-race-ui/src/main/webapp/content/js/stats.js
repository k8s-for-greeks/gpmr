/*!
 * Stats.js (https://github.com/angusgibbs/stats.js)
 * Copyright 2012 Angus Gibbs
 */
(function(root) {
	// Create the top level stats object
	// =================================
	
	// Wrapper to create a chainable stats object.
	//
	// arr - The array to work with.
	//
	// Returns a new chainable object.
	var stats = function(arr) {
		return new stats.init(arguments.length > 1 ?
			Array.prototype.slice.call(arguments, 0) :
			arr);
	};

	// Creates a new chainable array object.
	//
	// arr - The array to work with.
	//
	// Returns a new chainable object.
	stats.init = function(arr) {
		this.arr = arr;
		this.length = arr.length;
	};

	// Define the methods for the stats object
	stats.init.prototype = {
		// Calls a function on each element in an array or JSON object.
		//
		// fn  - The function to call on each element.
		//       el    - The array or object element
		//       index - The index or key of the array element
		//       arr   - The array or object
		//
		// Returns nothing.
		each: function(fn) {
			if (this.arr.length === undefined) {
				// The wrapped array is a JSON object
				for (var key in arr) {
					fn.call(this.arr[key], key, this.arr[key], this.arr);
				}
			} else {
				// The wrapper array is an array
				for (var i = 0, l = this.arr.length; i < l; i++) {
					fn.call(this.arr[i], this.arr[i], i, this.arr);
				}
			}

			return this;
		},

		// Replaces each element in an array or JSON object with the result of
		// the function that is called against each element.
		//
		// fn - The function to call on each element
		//      el    - The array or object element
		//      index - The index or key of the array element
		//
		// Returns nothing.
		map: function(fn) {
			var arr = this.arr;

			if (arr.length === undefined) {
				// The wrapped array is a JSON object
				for (var key in arr) {
					this.arr[key] = fn.call(arr[key], arr[key], key, arr);
				}
			} else {
				// The wrapped array is an array
				for (var i = 0, l = this.arr.length; i < l; i++) {
					this.arr[i] = fn.call(arr[i], arr[i], i, arr);
				}
			}

			return this;
		},

		// Replaces each element of the array with the attribute of that given
		// element.
		//
		// attr - The attribute to pluck.
		//
		// Returns nothing.
		pluck: function(attr) {
			var newArr = [];

			if (this.arr.length === undefined) {
				// The wrapped array is a JSON object
				for (var key in arr) {
					newArr.push(this.arr[key][attr]);
				}
			} else {
				// The wrapped array is an array
				for (var i = 0, l = this.arr.length; i < l; i++) {
					newArr.push(this.arr[i][attr]);
				}
			}

			return stats(newArr);
		},

		// Finds the smallest number.
		//
		// attr - Optional. If passed, the elemnt with the minimum value for the
		//        given attribute will be returned.
		//
		// Returns the minimum.
		min: function(attr) {
			// Get the numbers
			var arr = this.arr;

			// Go through each of the numbers and find the minimum
			var minimum = attr == null ? arr[0] : arr[0][attr];
			var minimumEl = attr == null ? arr[0] : arr[0];

			stats(arr).each(function(num, index) {
				if ((attr == null ? num : num[attr]) < minimum) {
					minimum = attr == null ? num : num[attr];
					minimumEl = num;
				}
			});

			return minimumEl;
		},

		// Finds the largest number.
		//
		// attr - Optional. If passed, the elemnt with the maximum value for the
		//        given attribute will be returned.
		//
		// Returns the maximum.
		max: function(attr) {
			// Get the numbers
			var arr = this.arr;

			// Go through each of the numbers and find the maximum
			var maximum = attr == null ? arr[0] : arr[0][attr];
			var maximumEl = attr == null ? arr[0] : arr[0];

			stats(arr).each(function(num, index) {
				if ((attr == null ? num : num[attr]) > maximum) {
					maximum = attr == null ? num : num[attr];
					maximumEl = num;
				}
			});

			return maximumEl;
		},

		// Finds the median of the numbers.
		//
		// Returns the median.
		median: function() {
			// Sort the numbers
			var arr = this.clone().sort().toArray();

			if (arr.length % 2 === 0) {
				// There are an even number of elements in the array; the median
				// is the average of the middle two
				return (arr[arr.length / 2 - 1] + arr[arr.length / 2]) / 2;
			} else {
				// There are an odd number of elements in the array; the median
				// is the middle one
				return arr[(arr.length - 1) / 2];
			}
		},

		// Finds the first quartile of the numbers.
		//
		// Returns the first quartile.
		q1: function() {
			// Sort the numbers
			var nums = this.clone().sort();

			// The first quartile is the median of the lower half of the numbers
			return nums.slice(0, Math.floor(nums.size() / 2)).median();
		},

		// Finds the third quartile of the numbers.
		//
		// Returns the third quartile.
		q3: function() {
			// Sort the numbers
			var nums = this.clone().sort();

			// The third quartile is the median of the upper half of the numbers
			return nums.slice(Math.ceil(nums.size() / 2)).median();
		},

		// Finds the interquartile range of the data set.
		//
		// Returns the IQR.
		iqr: function() {
			return this.q3() - this.q1();
		},
			
		// Finds all outliers in the data set, using the 1.5 * IQR away from
		// the median test.
		//
		// Returns a new stats object with the outliers.
		findOutliers: function() {
			// Get the median and the range that the number must fall within
			var median = this.median();
			var range  = this.iqr() * 1.5;

			// Create a new stats object to hold the outliers
			var outliers = stats([]);

			// Go through each element in the data set and test to see if it
			// is an outlier
			this.each(function(num) {
				if (Math.abs(num - median) > range) {
					// The number is an outlier
					outliers.push(num);
				}
			});

			return outliers;
		},

		// Tests if the given number would be an outlier in the data set.
		//
		// num - The number to test.
		//
		// Returns a boolean.
		testOutlier: function(num) {
			return (Math.abs(num - this.median()) > this.iqr() * 1.5);
		},

		// Removes all the outliers from the data set.
		//
		// Returns nothing.
		removeOutliers: function() {
			// Get the median and the range that the number must fall within
			var median = this.median();
			var range  = this.iqr() * 1.5;

			// Create a new stats object that will hold all the non-outliers
			var notOutliers = stats([]);

			// Go through each element in the data set and test to see if it
			// is an outlier
			this.each(function(num) {
				if (Math.abs(num - median) <= range) {
					// The number is not an outlier
					notOutliers.push(num);
				}
			});

			return notOutliers;
		},

		// Finds the mean of the numbers.
		//
		// Returns the mean.
		mean: function() {
			return this.sum() / this.size();
		},

		// Finds the sum of the numbers.
		//
		// Returns the sum.
		sum: function() {
			var result = 0;

			this.each(function(num) {
				result += num;
			});

			return result;
		},

		// Finds the standard deviation of the numbers.
		//
		// Returns the standard deviation.
		stdDev: function() {
			// Get the mean
			var mean = this.mean();

			// Get a new stats object to work with
			var nums = this.clone();

			// Map each element of nums to the square of the element minus the
			// mean
			nums.map(function(num) {
				return Math.pow(num - mean, 2);
			});

			// Return the standard deviation
			return Math.sqrt(nums.sum() / (nums.size() - 1));
		},

		// Calculates the correlation coefficient for the data set.
		//
		// Returns the value of r.
		r: function() {
			// Get the x and y coordinates
			var xCoords = this.pluck('x');
			var yCoords = this.pluck('y');

			// Get the means for the x and y coordinates
			var meanX = xCoords.mean();
			var meanY = yCoords.mean();

			// Get the standard deviations for the x and y coordinates
			var stdDevX = xCoords.stdDev();
			var stdDevY = yCoords.stdDev();

			// Map each element to the difference of the element and the mean
			// divided by the standard deviation
			xCoords.map(function(num) {
				return (num - meanX) / stdDevX;
			});
			yCoords.map(function(num) {
				return (num - meanY) / stdDevY;
			});

			// Multiply each element in the x by the corresponding value in
			// the y
			var nums = this.clone().map(function(num, index) {
				return xCoords.get(index) * yCoords.get(index);
			});

			// r is the sum of xCoords over the number of points minus 1
			return nums.sum() / (nums.size() - 1);
		},

		// Calculates the Least Squares Regression line for the data set.
		//
		// Returns an object with the slope and y intercept.
		linReg: function() {
			// Get the x and y coordinates
			var xCoords = this.pluck('x');
			var yCoords = this.pluck('y');

			// Get the means for the x and y coordinates
			var meanX = xCoords.mean();
			var meanY = yCoords.mean();

			// Get the standard deviations for the x and y coordinates
			var stdDevX = xCoords.stdDev();
			var stdDevY = yCoords.stdDev();

			// Calculate the correlation coefficient
			var r = this.r();

			// Calculate the slope
			var slope = r * (stdDevY / stdDevX);

			// Calculate the y-intercept
			var yIntercept = meanY - slope * meanX;

			return {
				slope: slope,
				yIntercept: yIntercept,
				r: r
			};
		},

		// Calculates the exponential regression line for the data set.
		//
		// Returns an object with the coefficient, base, and correlation
		// coefficient for the linearized data.
		expReg: function() {
			// Get y coordinates
			var yCoords = this.pluck('y');

			// Do a semi-log transformation of the coordinates
			yCoords.map(function(num) {
				return Math.log(num);
			});

			// Get a new stats object to work with that has the transformed data
			var nums = this.clone().map(function(coord, index) {
				return {
					x: coord.x,
					y: yCoords.get(index)
				};
			});

			// Calculate the linear regression for the linearized data
			var linReg = nums.linReg();

			// Calculate the coefficient for the exponential equation
			var coefficient = Math.pow(Math.E, linReg.yIntercept);

			// Calculate the base for the exponential equation
			var base = Math.pow(Math.E, linReg.slope);

			return {
				coefficient: coefficient,
				base: base,
				r: linReg.r
			};
		},

		// Calculates the power regression line for the data set.
		//
		// Returns an object with the coefficient, base, and correlation
		// coefficient for the linearized data.
		powReg: function() {
			// Get y coordinates
			var xCoords = this.pluck('x');
			var yCoords = this.pluck('y');

			// Do a log-log transformation of the coordinates
			xCoords.map(function(num) {
				return Math.log(num);
			});
			yCoords.map(function(num) {
				return Math.log(num);
			});

			// Get a new stats object to work with that has the transformed data
			var nums = this.clone().map(function(coord, index) {
				return {
					x: xCoords.get(index),
					y: yCoords.get(index)
				};
			});

			// Calculate the linear regression for the linearized data
			var linReg = nums.linReg();

			// Calculate the coefficient for the power equation
			var coefficient = Math.pow(Math.E, linReg.yIntercept);

			// Calculate the exponent for the power equation
			var exponent = linReg.slope;

			return {
				coefficient: coefficient,
				exponent: exponent,
				r: linReg.r
			};
		},

		// Returns the number of elements.
		size: function() {
			return this.arr.length;
		},

		// Clones the current stats object, providing a new stats object which
		// can be changed without modifying the original object.
		//
		// Returns a new stats object.
		clone: function() {
			return stats(this.arr.slice(0));
		},

		// Sorts the internal array, optionally by an attribute.
		//
		// attr - The attribute of the JSON object to sort by. (Optional.)
		//
		// Returns nothing.
		sort: function(attr) {
			// Create the sort function
			var sortFn;

			// CASE: Simple ascending sort
			if (attr == null) {
				sortFn = function(a, b) { return a - b; };
			}
			// CASE: Simple descending sort
			else if (attr === true) {
				sortFn = function(a, b) { return b - a; };
			}
			// CASE: Sort by an attribute
			else if (typeof attr === 'string') {
				sortFn = function(a, b) { return a[attr] - b[attr]; };
			}
			// CASE: Sort by a function
			else {
				sortFn = attr;
			}

			this.arr = this.arr.sort(sortFn);

			return this;
		},

		// Gets an element from the object.
		//
		// i - The index to retrieve.
		//
		// Returns the element at that index.
		get: function(i) {
			return this.arr[i];
		},

		// Sets an element on the object.
		//
		// i   - The index to set.
		// val - The value to set the index to.
		//
		// Returns nothing.
		set: function(i, val) {
			this.arr[i] = val;

			return this;
		},

		// Calculates the greatest common divisor of the set.
		//
		// Returns a Number, the gcd.
		gcd: function() {
			// Create a new stats object to work with
			var nums = this.clone();

			// Go through each element and make the element the gcd of it
			// and the element to its left
			for (var i = 1; i < nums.size(); i++) {
				nums.set(i, gcd(nums.get(i - 1), nums.get(i)));
			}

			// The gcd of all the numbers is now in the final element
			return nums.get(nums.size() - 1);
		},

		// Calculates the least common multiple of the set.
		//
		// Returns a Number, the lcm.
		lcm: function() {
			// Create a new stats object to work with
			var nums = this.clone();

			// Go through each element and make the element the lcm of it
			// and the element to its left
			for (var i = 1; i < nums.size(); i++) {
				nums.set(i, lcm(nums.get(i - 1), nums.get(i)));
			}

			// The lcm of all the numbers if now in the final element
			return nums.get(nums.size() - 1);
		}
	};

	// Private. Calculates the gcd of two numbers using Euclid's method.
	//
	// Returns a Number.
	function gcd(a, b) {
		if (b === 0) {
			return a;
		}

		return gcd(b, a - b * Math.floor(a / b));
	}

	// Private. Calculates the lcm of two numbers.
	//
	// Returns a Number.
	function lcm(a, b) {
		// The least common multiple is the absolute value of the product of
		// the numbers divided by the greatest common denominator
		return Math.abs(a * b) / gcd(a, b);
	}

	// Provide built in JavaScript array mutator methods for the data list
	var mutators = ['pop', 'push', 'shift', 'splice', 'unshift'];
	for (var i = 0; i < mutators.length; i++) {
		stats.init.prototype[mutators[i]] = (function(method) {
			return function() {
				return Array.prototype[method].apply(
					this.arr,
					Array.prototype.slice.call(arguments, 0)
				);
			};	
		})(mutators[i]);
	}

	// Provide built in JavaScript array accessor methods for the data list
	var accessors = ['concat', 'join', 'slice', 'reverse'];
	for (var i = 0; i < accessors.length; i++) {
		stats.init.prototype[accessors[i]] = (function(method) {
			return function() {
				this.arr = Array.prototype[method].apply(
					this.arr,
					Array.prototype.slice.call(arguments, 0)
				);

				return this;
			};	
		})(accessors[i]);
	}

	// Override the built-in #toJSON() and #toArray() methods
	stats.init.prototype.toJSON = stats.init.prototype.toArray = function() {
		return this.arr;
	};

	// Creates a list from the specified lower bound to the specified upper
	// bound.
	//
	// lower - The lower bound.
	// upper - The upper bound.
	// step  - The amount to count by.
	//
	// Returns a new stats object.
	stats.list = function(lower, upper, step) {
		// Create the array
		var arr = [];
		for (var i = lower; i <= upper; i += step || 1) {
			arr[i - lower] = i;
		}

		return stats(arr);
	};

	// Computes the factorial of a number.
	//
	// num - The number.
	//
	// Returns the factorial.
	stats.factorial = function(num) {
		// Handle the special case of 0
		if (num === 0) {
			return 1;
		}

		// Otherwise compute the factorial
		for (var i = num - 1; i > 1; i--) {
			num *= i;
		}

		return num;
	};

	// Computes a permutation.
	//
	// n - The length of the set.
	// r - The number of elements in the subset.
	//
	// Returns the permutation.
	stats.permutation = stats.nPr = function(n, r) {
		return stats.factorial(n) / stats.factorial(n - r);
	};

	// Computes a combination.
	//
	// n - The length of the set.
	// r - The number of elements in the subset.
	//
	// Returns the combination.
	stats.combination = stats.nCr = function(n, r) {
		return stats.factorial(n) /
			(stats.factorial(r) * stats.factorial(n - r));
	};

	// Computes the probability of a binomial event.
	//
	// trials - The number of trials.
	// p      - The probability of success.
	// x      - The event number (optional).
	//
	// If x is not passed, an array with all the probabilities
	// will be returned.
	//
	// Returns a number or an array.
	stats.binompdf = function(trials, p, x) {
		// CASE: Specific event was passed
		if (x != null) {
			// Return 0 if the event does not exist
			if (x > trials || x < 0) {
				return 0;
			}

			// Return the probability otherwise
			return stats.nCr(trials, x) * Math.pow(p, x) *
				Math.pow(1 - p, trials - x);
		}
		// CASE: No specific event was passed
		else {
			// Compute the probabilities
			return stats.list(0, trials).map(function(num) {
				return stats.binompdf(trials, p, num);
			}).toArray();
		}
	};

	// Computes the cumulative probability of a binomial event.
	//
	// trials - The number of trials.
	// p      - The probability of success.
	// x      - The upper bound (inclusive).
	//
	// Returns the probability.
	stats.binomcdf = function(trials, p, x) {
		return stats.list(0, x).map(function(num) {
			return stats.binompdf(trials, p, num);
		}).sum();
	};

	// Computes the probability of a geometric event.
	//
	// p - The probability of success.
	// x - The event number.
	//
	// Returns the probability.
	stats.geompdf = function(p, x) {
		return Math.pow(1 - p, x - 1) * p;
	};

	// Computes the cumulative probability of a geometric event.
	//
	// p - The probability of success.
	// x - The event number.
	//
	// Returns the probability.
	stats.geomcdf = function(p, x) {
		return stats.list(1, x).map(function(num) {
			return stats.geompdf(p, num);
		}).sum();
	};

	// Computes the normal probability of an event.
	//
	// x     - The event number.
	// mu    - The mean.
	// sigma - The standard deviation.
	//
	// Returns the probability.
	stats.normalpdf = function(x, mu, sigma) {
		return (1 / (sigma * Math.sqrt(2 * Math.PI))) *
			Math.exp(-1 * (x - mu) * (x - mu) /
			(2 * sigma * sigma));
	};

	// Computes the cumulative normal probability of an event.
	//
	// x - The event number.
	// mu - The mean.
	// sigma - The standard deviation.
	//
	// If four parameters are passed the first two are taken as the
	// low and high.
	//
	// Returns the probability.
	stats.normalcdf = function(x, mu, sigma) {
		// If four parameters were passed, return the difference of
		// the probabilities of the upper and lower
		if (arguments.length === 4) {
			// The cumulative probability is the difference of the
			// probabilities of the upper and the lower
			return stats.normalcdf(arguments[1], arguments[2], arguments[3]) -
				stats.normalcdf(arguments[0], arguments[2], arguments[3]);
		}


		// Convert the event to a z score
		var z = (x - mu) / sigma;

		// The probability will be stored here
		var p;

		// Coefficients
		var p0 = 220.2068679123761;
		var p1 = 221.2135961699311;
		var p2 = 112.0792914978709;
		var p3 = 33.91286607838300;
		var p4 = 6.373962203531650;
		var p5 = .7003830644436881;
		var p6 = .03526249659989109;

		var q0 = 440.4137358247522;
		var q1 = 793.8265125199484;
		var q2 = 637.3336333788311;
		var q3 = 296.5642487796737;
		var q4 = 86.78073220294608;
		var q5 = 16.06417757920695;
		var q6 = 1.755667163182642;
		var q7 = .08838834764831844;

		// Another coefficient (10/sqrt(2))
		var cutoff = 7.071;

		// Cache the absolute value of the z score
		var zabs = Math.abs(z);

		// If you're more than 37 z scores away just return 1 or 0
		if (z > 37) {
			return 1;
		}
		if (z < -37) {
			return 0.0;
		}

		// Compute the normalpdf of this event
		var exp = Math.exp(-.5 * zabs * zabs);
		var pdf = exp / Math.sqrt(2 * Math.PI);

		// Compute the probability
		if (zabs < cutoff) {
			p = exp*((((((p6*zabs + p5)*zabs + p4)*zabs + p3)*zabs +
				p2)*zabs + p1)*zabs + p0)/(((((((q7*zabs + q6)*zabs +
				q5)*zabs + q4)*zabs + q3)*zabs + q2)*zabs + q1)*zabs +
				q0);
		}
		else {
			p = pdf/(zabs + 1.0/(zabs + 2.0/(zabs + 3.0/(zabs + 4.0/
				(zabs + 0.65)))));
		}

		if (z < 0.0) {
			return p;
		} else {
			return 1 - p;
		}
	};

	// Computes the inverse normal.
	//
	// p - The probability of x being less than or equal to *x*, the value we
	//     are looking for.
	//
	// Returns the x value.
	stats.invNorm = function(p) {
		// Coefficients for the rational approximation
		var a = [
			-3.969683028665376e+01,
			 2.209460984245205e+02,
			-2.759285104469687e+02,
			 1.383577518672690e+02,
			-3.066479806614716e+01,
			 2.506628277459239e+00
		];
		var b = [
			-5.447609879822406e+01,
			 1.615858368580409e+02,
			-1.556989798598866e+02,
			 6.680131188771972e+01,
			-1.328068155288572e+01
		];
		var c = [
			-7.784894002430293e-03,
			-3.223964580411365e-01,
			-2.400758277161838e+00,
			-2.549732539343734e+00,
			 4.374664141464968e+00,
			 2.938163982698783e+00
		];
		var d = [
			7.784695709041462e-03,
			3.224671290700398e-01,
			2.445134137142996e+00,
			3.754408661907416e+00
		];

		// Breakpoints
		var pLow = .02425;
		var pHigh = 1 - pLow;

		// Rational appoximation for the lower region
		if (0 < p && p < pLow) {
			var q = Math.sqrt(-2 * Math.log(p));
			return (((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) /
				((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) *q + 1);
		}
		else if (pLow <= p && p <= pHigh) {
			var q = p - 0.5;
			var r = q * q;
			return (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4]) * r + a[5]) * q /
				(((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4]) * r + 1);
		}
		else if (pHigh < p && p < 1) {
			var q = Math.sqrt(-2 * Math.log(1 - p));
			return -(((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) /
				((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
		}
	};

	// Runs a 1-mean z-test.
	//
	// muZero     - The proposed population mean.
	// sigma      - The standard deviation of the population.
	// xBar       - The sample mean.
	// n          - The sample size.
	// inequality - One of "notequal", "lessthan", or "greaterthan", depending
	//              on what you're testing 
	//
	// Returns an object with the z-test statistic (z) and P-value (p).
	stats.ZTest = function(muZero, sigma, xBar, n, inequality) {
		// Calculate the z-test statistic
		var z = (xBar - muZero) / (sigma / Math.sqrt(n));
		
		// Calculate the P-value
		var p;
		if (z < 0) {
			// Use normalcdf from -infinity to the z statistic
			p = stats.normalcdf(-Infinity, z, 0, 1);
		}
		else {
			// Use normalcdf from the z statistic to +infinity
			p = stats.normalcdf(z, Infinity, 0, 1);
		}

		// Multiply the P-value by two if you're doing a not equal test
		if (inequality === 'notequal') {
			p *= 2;
		}

		return {
			z: z,
			p: p
		};
	};

	// Computes a 1-mean z-interval.
	//
	// sigma - The population standard deviation.
	// xBar  - The sample mean.
	// n     - The sample size.
	// level - The confidence level (between 0 and 1, exclusive).
	//
	// Returns an object with the low, high, and margin of error (moe).
	stats.ZInterval = function(sigma, xBar, n, level) {
		// Compute the margin of error
		var moe = -stats.invNorm((1 - level) / 2) * sigma / Math.sqrt(n);

		return {
			low: xBar - moe,
			high: xBar + moe,
			moe: moe
		};
	};

	// Export the stats object
	// =================================================
	if (typeof define === 'function') {
		// Expose to AMD loaders
		define(function() {
			return stats;
		});
	} else if (typeof module !== 'undefined' && module.exports) {
		// Expose to Node and similar environments
		module.exports = stats;
	} else {
		// Just write to window (or whatever is the root object)
		root.stats = stats;
	}
}(this));
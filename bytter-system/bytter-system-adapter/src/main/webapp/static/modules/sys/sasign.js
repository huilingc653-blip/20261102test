(function (exports) {
  "use strict";

  if (Math.trunc == undefined) {
    Math.trunc = function (x) {
      return x < 0 ? 0 - Math.floor(0 - x) : Math.floor(x);
    };
  }
  if ((-2147483649).toString(16) === "-0") { // Opera 12
    var numberToString = Number.prototype.toString;
    Number.prototype.toString = function (radix) {
      "use strict";
      return (this < 0 ? "-" : "") + numberToString.call(this < 0 ? 0 - this : this, radix);
    };
  }

  // BigInteger.js
  // Available under Public Domain
  // https://github.com/Yaffle/BigInteger/

  // For implementation details, see "The Handbook of Applied Cryptography"
  // http://www.cacr.math.uwaterloo.ca/hac/about/chap14.pdf

  var parseInteger = function (s, from, to, radix) {
    var i = from - 1;
    var n = 0;
    var y = radix < 10 ? radix : 10;
    while (++i < to) {
      var code = s.charCodeAt(i);
      var v = code - 48;
      if (v < 0 || y <= v) {
        v = 10 - 65 + code;
        if (v < 10 || radix <= v) {
          v = 10 - 97 + code;
          if (v < 10 || radix <= v) {
            throw new RangeError();
          }
        }
      }
      n = n * radix + v;
    }
    return n;
  };

  var createArray = function (length) {
    var x = new Array(length);
    var i = -1;
    while (++i < length) {
      x[i] = 0;
    }
    return x;
  };

  // count >= 1
  var pow = function (x, count) {
    var accumulator = 1;
    var v = x;
    var c = count;
    while (c > 1) {
      var q = Math.trunc(c / 2);
      if (q * 2 !== c) {
        accumulator *= v;
      }
      v *= v;
      c = q;
    }
    return accumulator * v;
  };

  var EPSILON = (function (x) {
    return x(x, 1 / 4503599627370496);
  }(function (f, epsilon) {
    return (1 + epsilon / 2) !== 1 ? f(f, epsilon / 2) : epsilon;
  }));
  var BASE = 2 / EPSILON;
  var SPLIT = 67108864 * pow(2, Math.trunc((Math.trunc(Math.log(BASE) / Math.log(2) + 0.5) - 53) / 2) + 1) + 1;

  var fastTrunc = function (x) {
    var v = (x - BASE) + BASE;
    return v > x ? v - 1 : v;
  };

  // Veltkamp-Dekker's algorithm
  // see http://web.mit.edu/tabbott/Public/quaddouble-debian/qd-2.3.4-old/docs/qd.pdf
  // with FMA:
  // var product = a * b;
  // var error = Math.fma(a, b, -product);
  var performMultiplication = function (carry, a, b) {
    var at = SPLIT * a;
    var ahi = at - (at - a);
    var alo = a - ahi;
    var bt = SPLIT * b;
    var bhi = bt - (bt - b);
    var blo = b - bhi;
    var product = a * b;
    var error = ((ahi * bhi - product) + ahi * blo + alo * bhi) + alo * blo;

    var hi = fastTrunc(product / BASE);
    var lo = product - hi * BASE + error;

    if (lo < 0) {
      lo += BASE;
      hi -= 1;
    }

    lo += carry - BASE;
    if (lo < 0) {
      lo += BASE;
    } else {
      hi += 1;
    }

    return {lo: lo, hi: hi};
  };

  var performDivision = function (a, b, divisor) {
    if (a >= divisor) {
      throw new RangeError();
    }
    var p = a * BASE;
    var y = p / divisor;
    var r = p % divisor;
    var q = fastTrunc(y);
    if (y === q && r > divisor - r) {
      q -= 1;
    }
    r += b - divisor;
    if (r < 0) {
      r += divisor;
    } else {
      q += 1;
    }
    y = fastTrunc(r / divisor);
    r -= y * divisor;
    q += y;
    return {q: q, r: r};
  };

  function BigInteger(sign, magnitude, length) {
    this.sign = sign;
    this.magnitude = magnitude;
    this.length = length;
  }

  var createBigInteger = function (sign, magnitude, length) {
    return length === 0 ? 0 : (length === 1 ? (sign === 1 ? 0 - magnitude[0] : magnitude[0]) : new BigInteger(sign, magnitude, length));
  };

  var parseBigInteger = function (s, radix) {
    if (radix === undefined) {
      radix = 10;
    }
    if (Math.trunc(radix) !== radix || !(radix >= 2 && radix <= 36)) {
      throw new RangeError("radix argument must be an integer between 2 and 36");
    }
    var length = s.length;
    if (length === 0) {
      throw new RangeError();
    }
    var sign = 0;
    var signCharCode = s.charCodeAt(0);
    var from = 0;
    if (signCharCode === 43) { // "+"
      from = 1;
    }
    if (signCharCode === 45) { // "-"
      from = 1;
      sign = 1;
    }

    length -= from;
    if (length === 0) {
      throw new RangeError();
    }
    if (pow(radix, length) <= BASE) {
      var value = parseInteger(s, from, from + length, radix);
      return sign === 1 ? 0 - value : value;
    }
    var groupLength = 0;
    var groupRadix = 1;
    var limit = fastTrunc(BASE / radix);
    while (groupRadix <= limit) {
      groupLength += 1;
      groupRadix *= radix;
    }
    var size = Math.trunc((length - 1) / groupLength) + 1;

    var magnitude = createArray(size);
    var k = size;
    var i = length;
    while (i > 0) {
      k -= 1;
      magnitude[k] = parseInteger(s, from + (i > groupLength ? i - groupLength : 0), from + i, radix);
      i -= groupLength;
    }

    var j = -1;
    while (++j < size) {
      var c = magnitude[j];
      var l = -1;
      while (++l < j) {
        var tmp = performMultiplication(c, magnitude[l], groupRadix, magnitude, l);
        var lo = tmp.lo;
        var hi = tmp.hi;
        magnitude[l] = lo;
        c = hi;
      }
      magnitude[j] = c;
    }

    while (size > 0 && magnitude[size - 1] === 0) {
      size -= 1;
    }

    return createBigInteger(size === 0 ? 0 : sign, magnitude, size);
  };

  var compareMagnitude = function (aMagnitude, aLength, aValue, bMagnitude, bLength, bValue) {
    if (aLength !== bLength) {
      return aLength < bLength ? -1 : +1;
    }
    var i = aLength;
    while (--i >= 0) {
      if ((aMagnitude === undefined ? aValue : aMagnitude[i]) !== (bMagnitude === undefined ? bValue : bMagnitude[i])) {
        return (aMagnitude === undefined ? aValue : aMagnitude[i]) < (bMagnitude === undefined ? bValue : bMagnitude[i]) ? -1 : +1;
      }
    }
    return 0;
  };

  var compareTo = function (aSign, aMagnitude, aLength, aValue, bSign, bMagnitude, bLength, bValue) {
    if (aSign === bSign) {
      var c = compareMagnitude(aMagnitude, aLength, aValue, bMagnitude, bLength, bValue);
      return aSign === 1 ? 0 - c : c; // positive zero will be returned for c === 0
    }
    return aSign === 1 ? -1 : +1;
  };

  var add = function (aSign, aMagnitude, aLength, aValue, bSign, bMagnitude, bLength, bValue) {
    var z = compareMagnitude(aMagnitude, aLength, aValue, bMagnitude, bLength, bValue);
    var minSign = z < 0 ? aSign : bSign;
    var minMagnitude = z < 0 ? aMagnitude : bMagnitude;
    var minLength = z < 0 ? aLength : bLength;
    var minValue = z < 0 ? aValue : bValue;
    var maxSign = z < 0 ? bSign : aSign;
    var maxMagnitude = z < 0 ? bMagnitude : aMagnitude;
    var maxLength = z < 0 ? bLength : aLength;
    var maxValue = z < 0 ? bValue : aValue;

    // |a| <= |b|
    if (minLength === 0) {
      return maxMagnitude === undefined ? (maxSign === 1 ? 0 - maxValue : maxValue) : createBigInteger(maxSign, maxMagnitude, maxLength);
    }
    var subtract = 0;
    var resultLength = maxLength;
    if (minSign !== maxSign) {
      subtract = 1;
      if (minLength === resultLength) {
        while (resultLength > 0 && (minMagnitude === undefined ? minValue : minMagnitude[resultLength - 1]) === (maxMagnitude === undefined ? maxValue : maxMagnitude[resultLength - 1])) {
          resultLength -= 1;
        }
      }
      if (resultLength === 0) { // a === (-b)
        return createBigInteger(0, createArray(0), 0);
      }
    }
    // result !== 0
    var result = createArray(resultLength + (1 - subtract));
    var i = -1;
    var c = 0;
    while (++i < resultLength) {
      var aDigit = i < minLength ? (minMagnitude === undefined ? minValue : minMagnitude[i]) : 0;
      c += (maxMagnitude === undefined ? maxValue : maxMagnitude[i]) + (subtract === 1 ? 0 - aDigit : aDigit - BASE);
      if (c < 0) {
        result[i] = BASE + c;
        c = 0 - subtract;
      } else {
        result[i] = c;
        c = 1 - subtract;
      }
    }
    if (c !== 0) {
      result[resultLength] = c;
      resultLength += 1;
    }
    while (resultLength > 0 && result[resultLength - 1] === 0) {
      resultLength -= 1;
    }
    return createBigInteger(maxSign, result, resultLength);
  };

  var multiply = function (aSign, aMagnitude, aLength, aValue, bSign, bMagnitude, bLength, bValue) {
    if (aLength === 0 || bLength === 0) {
      return createBigInteger(0, createArray(0), 0);
    }
    var resultSign = aSign === 1 ? 1 - bSign : bSign;
    if (aLength === 1 && (aMagnitude === undefined ? aValue : aMagnitude[0]) === 1) {
      return bMagnitude === undefined ? (resultSign === 1 ? 0 - bValue : bValue) : createBigInteger(resultSign, bMagnitude, bLength);
    }
    if (bLength === 1 && (bMagnitude === undefined ? bValue : bMagnitude[0]) === 1) {
      return aMagnitude === undefined ? (resultSign === 1 ? 0 - aValue : aValue) : createBigInteger(resultSign, aMagnitude, aLength);
    }
    var resultLength = aLength + bLength;
    var result = createArray(resultLength);
    var i = -1;
    while (++i < bLength) {
      var c = 0;
      var j = -1;
      while (++j < aLength) {
        var carry = 0;
        c += result[j + i] - BASE;
        if (c >= 0) {
          carry = 1;
        } else {
          c += BASE;
        }
        var tmp = performMultiplication(c, aMagnitude === undefined ? aValue : aMagnitude[j], bMagnitude === undefined ? bValue : bMagnitude[i]);
        var lo = tmp.lo;
        var hi = tmp.hi;
        result[j + i] = lo;
        c = hi + carry;
      }
      result[aLength + i] = c;
    }
    while (resultLength > 0 && result[resultLength - 1] === 0) {
      resultLength -= 1;
    }
    return createBigInteger(resultSign, result, resultLength);
  };

  var divideAndRemainder = function (aSign, aMagnitude, aLength, aValue, bSign, bMagnitude, bLength, bValue, divide) {
    if (bLength === 0) {
      throw new RangeError();
    }
    if (aLength === 0) {
      return createBigInteger(0, createArray(0), 0);
    }
    var quotientSign = aSign === 1 ? 1 - bSign : bSign;
    if (bLength === 1 && (bMagnitude === undefined ? bValue : bMagnitude[0]) === 1) {
      if (divide === 1) {
        return aMagnitude === undefined ? (quotientSign === 1 ? 0 - aValue : aValue) : createBigInteger(quotientSign, aMagnitude, aLength);
      }
      return createBigInteger(0, createArray(0), 0);
    }

    var divisorOffset = aLength + 1; // `+ 1` for extra digit in case of normalization
    var divisorAndRemainder = createArray(divisorOffset + bLength + 1); // `+ 1` to avoid `index < length` checks
    var divisor = divisorAndRemainder;
    var remainder = divisorAndRemainder;
    var n = -1;
    while (++n < aLength) {
      remainder[n] = aMagnitude === undefined ? aValue : aMagnitude[n];
    }
    var m = -1;
    while (++m < bLength) {
      divisor[divisorOffset + m] = bMagnitude === undefined ? bValue : bMagnitude[m];
    }

    var top = divisor[divisorOffset + bLength - 1];

    // normalization
    var lambda = 1;
    if (bLength > 1) {
      lambda = fastTrunc(BASE / (top + 1));
      if (lambda > 1) {
        var carry = 0;
        var l = -1;
        while (++l < divisorOffset + bLength) {
          var tmp = performMultiplication(carry, divisorAndRemainder[l], lambda);
          var lo = tmp.lo;
          var hi = tmp.hi;
          divisorAndRemainder[l] = lo;
          carry = hi;
        }
        divisorAndRemainder[divisorOffset + bLength] = carry;
        top = divisor[divisorOffset + bLength - 1];
      }
      // assertion
      if (top < fastTrunc(BASE / 2)) {
        throw new RangeError();
      }
    }

    var shift = aLength - bLength + 1;
    if (shift < 0) {
      shift = 0;
    }
    var quotient = undefined;
    var quotientLength = 0;

    var i = shift;
    while (--i >= 0) {
      var t = bLength + i;
      var q = BASE - 1;
      if (remainder[t] !== top) {
        var tmp2 = performDivision(remainder[t], remainder[t - 1], top);
        var q2 = tmp2.q;
        var r2 = tmp2.r;
        q = q2;
      }

      var ax = 0;
      var bx = 0;
      var j = i - 1;
      while (++j <= t) {
        var rj = remainder[j];
        var tmp3 = performMultiplication(bx, q, divisor[divisorOffset + j - i]);
        var lo3 = tmp3.lo;
        var hi3 = tmp3.hi;
        remainder[j] = lo3;
        bx = hi3;
        ax += rj - remainder[j];
        if (ax < 0) {
          remainder[j] = BASE + ax;
          ax = -1;
        } else {
          remainder[j] = ax;
          ax = 0;
        }
      }
      while (ax !== 0) {
        q -= 1;
        var c = 0;
        var k = i - 1;
        while (++k <= t) {
          c += remainder[k] - BASE + divisor[divisorOffset + k - i];
          if (c < 0) {
            remainder[k] = BASE + c;
            c = 0;
          } else {
            remainder[k] = c;
            c = +1;
          }
        }
        ax += c;
      }
      if (divide === 1 && q !== 0) {
        if (quotientLength === 0) {
          quotientLength = i + 1;
          quotient = createArray(quotientLength);
        }
        quotient[i] = q;
      }
    }

    if (divide === 1) {
      if (quotientLength === 0) {
        return createBigInteger(0, createArray(0), 0);
      }
      return createBigInteger(quotientSign, quotient, quotientLength);
    }

    var remainderLength = aLength + 1;
    if (lambda > 1) {
      var r = 0;
      var p = remainderLength;
      while (--p >= 0) {
        var tmp4 = performDivision(r, remainder[p], lambda);
        var q4 = tmp4.q;
        var r4 = tmp4.r;
        remainder[p] = q4;
        r = r4;
      }
      if (r !== 0) {
        // assertion
        throw new RangeError();
      }
    }
    while (remainderLength > 0 && remainder[remainderLength - 1] === 0) {
      remainderLength -= 1;
    }
    if (remainderLength === 0) {
      return createBigInteger(0, createArray(0), 0);
    }
    var result = createArray(remainderLength);
    var o = -1;
    while (++o < remainderLength) {
      result[o] = remainder[o];
    }
    return createBigInteger(aSign, result, remainderLength);
  };

  var toString = function (sign, magnitude, length, radix) {
    var result = sign === 1 ? "-" : "";

    var remainderLength = length;
    if (remainderLength === 0) {
      return "0";
    }
    if (remainderLength === 1) {
      result += magnitude[0].toString(radix);
      return result;
    }
    var groupLength = 0;
    var groupRadix = 1;
    var limit = fastTrunc(BASE / radix);
    while (groupRadix <= limit) {
      groupLength += 1;
      groupRadix *= radix;
    }
    // assertion
    if (groupRadix * radix <= BASE) {
      throw new RangeError();
    }
    var size = remainderLength + Math.trunc((remainderLength - 1) / groupLength) + 1;
    var remainder = createArray(size);
    var n = -1;
    while (++n < remainderLength) {
      remainder[n] = magnitude[n];
    }

    var k = size;
    while (remainderLength !== 0) {
      var groupDigit = 0;
      var i = remainderLength;
      while (--i >= 0) {
        var tmp = performDivision(groupDigit, remainder[i], groupRadix);
        var q = tmp.q;
        var r = tmp.r;
        remainder[i] = q;
        groupDigit = r;
      }
      while (remainderLength > 0 && remainder[remainderLength - 1] === 0) {
        remainderLength -= 1;
      }
      k -= 1;
      remainder[k] = groupDigit;
    }
    result += remainder[k].toString(radix);
    while (++k < size) {
      var t = remainder[k].toString(radix);
      var j = groupLength - t.length;
      while (--j >= 0) {
        result += "0";
      }
      result += t;
    }
    return result;
  };

  BigInteger.prototype.toString = function (radix) {
    if (radix === undefined) {
      radix = 10;
    }
    if (Math.trunc(radix) !== radix || !(radix >= 2 && radix <= 36)) {
      throw new RangeError("radix argument must be an integer between 2 and 36");
    }
    return toString(this.sign, this.magnitude, this.length, radix);
  };

  var sign = function (x) {
    return x < 0 ? 1 : 0;
  };

  var length = function (x) {
    return 0 + x === 0 ? 0 : 1;
  };

  var abs = function (x) {
    return x < 0 ? 0 - x : 0 + x;
  };

  var compareTo0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        return x < y ? -1 : (y < x ? +1 : 0);
      } else {
        return compareTo(sign(x), undefined, length(x), abs(x), y.sign, y.magnitude, y.length, 0);
      }
    } else {
      if (typeof y === "number") {
        return compareTo(x.sign, x.magnitude, x.length, 0, sign(y), undefined, length(y), abs(y));
      } else {
        return compareTo(x.sign, x.magnitude, x.length, 0, y.sign, y.magnitude, y.length, 0);
      }
    }
  };
  var add0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        var value = x + y;
        if (value > -BASE && value < +BASE) {
          return value;
        }
        return add(sign(x), undefined, length(x), abs(x), sign(y), undefined, length(y), abs(y));
      } else {
        return add(sign(x), undefined, length(x), abs(x), y.sign, y.magnitude, y.length, 0);
      }
    } else {
      if (typeof y === "number") {
        return add(x.sign, x.magnitude, x.length, 0, sign(y), undefined, length(y), abs(y));
      } else {
        return add(x.sign, x.magnitude, x.length, 0, y.sign, y.magnitude, y.length, 0);
      }
    }
  };
  var subtract0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        var value = x - y;
        if (value > -BASE && value < +BASE) {
          return value;
        }
        return add(sign(x), undefined, length(x), abs(x), 1 - sign(y), undefined, length(y), abs(y));
      } else {
        return add(sign(x), undefined, length(x), abs(x), 1 - y.sign, y.magnitude, y.length, 0);
      }
    } else {
      if (typeof y === "number") {
        return add(x.sign, x.magnitude, x.length, 0, 1 - sign(y), undefined, length(y), abs(y));
      } else {
        return add(x.sign, x.magnitude, x.length, 0, 1 - y.sign, y.magnitude, y.length, 0);
      }
    }
  };
  var multiply0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        var value = 0 + x * y;
        if (value > -BASE && value < +BASE) {
          return value;
        }
        return multiply(sign(x), undefined, length(x), abs(x), sign(y), undefined, length(y), abs(y));
      } else {
        return multiply(sign(x), undefined, length(x), abs(x), y.sign, y.magnitude, y.length, 0);
      }
    } else {
      if (typeof y === "number") {
        return multiply(x.sign, x.magnitude, x.length, 0, sign(y), undefined, length(y), abs(y));
      } else {
        return multiply(x.sign, x.magnitude, x.length, 0, y.sign, y.magnitude, y.length, 0);
      }
    }
  };
  var divide0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        if (0 + y === 0) {
          throw new RangeError();
        }
        // `0 + Math.trunc(x / y)` is slow in Chrome
        return x < 0 ? (y < 0 ? 0 + Math.floor((0 - x) / (0 - y)) : 0 - Math.floor((0 - x) / (0 + y))) : (y < 0 ? 0 - Math.floor((0 + x) / (0 - y)) : 0 + Math.floor((0 + x) / (0 + y)));
      } else {
        return divideAndRemainder(sign(x), undefined, length(x), abs(x), y.sign, y.magnitude, y.length, 0, 1);
      }
    } else {
      if (typeof y === "number") {
        return divideAndRemainder(x.sign, x.magnitude, x.length, 0, sign(y), undefined, length(y), abs(y), 1);
      } else {
        return divideAndRemainder(x.sign, x.magnitude, x.length, 0, y.sign, y.magnitude, y.length, 0, 1);
      }
    }
  };
  var remainder0 = function (x, y) {
    if (typeof x === "number") {
      if (typeof y === "number") {
        if (0 + y === 0) {
          throw new RangeError();
        }
        return 0 + x % y;
      } else {
        return divideAndRemainder(sign(x), undefined, length(x), abs(x), y.sign, y.magnitude, y.length, 0, 0);
      }
    } else {
      if (typeof y === "number") {
        return divideAndRemainder(x.sign, x.magnitude, x.length, 0, sign(y), undefined, length(y), abs(y), 0);
      } else {
        return divideAndRemainder(x.sign, x.magnitude, x.length, 0, y.sign, y.magnitude, y.length, 0, 0);
      }
    }
  };
  var negate0 = function (x) {
    if (typeof x === "number") {
      return 0 - x;
    } else {
      return createBigInteger(1 - x.sign, x.magnitude, x.length);
    }
  };

  BigInteger.parseInt = parseBigInteger;
  BigInteger.compareTo = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      return x < y ? -1 : (y < x ? +1 : 0);
    }
    return compareTo0(x, y);
  };
  BigInteger.add = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      var value = x + y;
      if (value > -BASE && value < +BASE) {
        return value;
      }
    }
    return add0(x, y);
  };
  BigInteger.subtract = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      var value = x - y;
      if (value > -BASE && value < +BASE) {
        return value;
      }
    }
    return subtract0(x, y);
  };
  BigInteger.multiply = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      var value = 0 + x * y;
      if (value > -BASE && value < +BASE) {
        return value;
      }
    }
    return multiply0(x, y);
  };
  BigInteger.divide = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      if (0 + y === 0) {
        throw new RangeError();
      }
      // `0 + Math.trunc(x / y)` is slow in Chrome
      return x < 0 ? (y < 0 ? 0 + Math.floor((0 - x) / (0 - y)) : 0 - Math.floor((0 - x) / (0 + y))) : (y < 0 ? 0 - Math.floor((0 + x) / (0 - y)) : 0 + Math.floor((0 + x) / (0 + y)));
    }
    return divide0(x, y);
  };
  BigInteger.remainder = function (x, y) {
    if (typeof x === "number" && typeof y === "number") {
      if (0 + y === 0) {
        throw new RangeError();
      }
      return 0 + x % y;
    }
    return remainder0(x, y);
  };
  BigInteger.negate = function (x) {
    if (typeof x === "number") {
      return 0 - x;
    }
    return negate0(x);
  };

  exports.BigInteger = BigInteger;

}(this));

/**
*
*  Base64 encode / decode
*
*  @author haitao.tu
*  @date   2010-04-26
*  @email  tuhaitao@foxmail.com
*
*/
 
function ESBase64() {
 
	// private property
	_keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
 
	// public method for encoding
	this.encode = function (input) {
		var output = "";
		var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
		var i = 0;
		input = _utf8_encode(input);
		while (i < input.length) {
			chr1 = input.charCodeAt(i++);
			chr2 = input.charCodeAt(i++);
			chr3 = input.charCodeAt(i++);
			enc1 = chr1 >> 2;
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			enc4 = chr3 & 63;
			if (isNaN(chr2)) {
				enc3 = enc4 = 64;
			} else if (isNaN(chr3)) {
				enc4 = 64;
			}
			output = output +
			_keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
			_keyStr.charAt(enc3) + _keyStr.charAt(enc4);
		}
		return output;
	}
 
	// public method for decoding
	this.decode = function (input) {
		var output = "";
		var chr1, chr2, chr3;
		var enc1, enc2, enc3, enc4;
		var i = 0;
		input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
		while (i < input.length) {
			enc1 = _keyStr.indexOf(input.charAt(i++));
			enc2 = _keyStr.indexOf(input.charAt(i++));
			enc3 = _keyStr.indexOf(input.charAt(i++));
			enc4 = _keyStr.indexOf(input.charAt(i++));
			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;
			output = output + String.fromCharCode(chr1);
			if (enc3 != 64) {
				output = output + String.fromCharCode(chr2);
			}
			if (enc4 != 64) {
				output = output + String.fromCharCode(chr3);
			}
		}
		output = _utf8_decode(output);
		return output;
	}
 
	// private method for UTF-8 encoding
	_utf8_encode = function (string) {
		string = string.replace(/\r\n/g,"\n");
		var utftext = "";
		for (var n = 0; n < string.length; n++) {
			var c = string.charCodeAt(n);
			if (c < 128) {
				utftext += String.fromCharCode(c);
			} else if((c > 127) && (c < 2048)) {
				utftext += String.fromCharCode((c >> 6) | 192);
				utftext += String.fromCharCode((c & 63) | 128);
			} else {
				utftext += String.fromCharCode((c >> 12) | 224);
				utftext += String.fromCharCode(((c >> 6) & 63) | 128);
				utftext += String.fromCharCode((c & 63) | 128);
			}
 
		}
		return utftext;
	}
 
	// private method for UTF-8 decoding
	_utf8_decode = function (utftext) {
		var string = "";
		var i = 0;
		var c = c1 = c2 = 0;
		while ( i < utftext.length ) {
			c = utftext.charCodeAt(i);
			if (c < 128) {
				string += String.fromCharCode(c);
				i++;
			} else if((c > 191) && (c < 224)) {
				c2 = utftext.charCodeAt(i+1);
				string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
				i += 2;
			} else {
				c2 = utftext.charCodeAt(i+1);
				c3 = utftext.charCodeAt(i+2);
				string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
				i += 3;
			}
		}
		return string;
	}
}

function iGetInnerText(testStr) {
        var resultStr = testStr.replace(/\ +/g, ""); //去掉空格
        resultStr = testStr.replace(/[ ]/g, "");    //去掉空格
        resultStr = testStr.replace(/[\r\n]/g, ""); //去掉回车换行
        return resultStr;
    }

var BTSN = '';
var isca = '';
var usercode = '';
var signType = 'SOFT';
var transType = 'SOFT';
var isie=false;
var uscode = '';
var qturl = "http://127.0.0.1:8173/crypto";
try {
/* if (navigator.appName.indexOf('Internet') >= 0 || navigator.appVersion.indexOf('Trident') >= 0) {
  if (window.navigator.cpuClass == 'x86') {
	  console.log("Is IE");
	  var WebSignClassID="<OBJECT ID=\"unisign\" name=\"unisign\" WIDTH=\"0\" HEIGHT=\"0\" CLASSID=\"CLSID:7A2520B4-22BB-4854-8C62-958E1F24CF02\" codebase=\"/nccloud/resources/platform/ca/SM2.CAB#version=1,0,0,12\"></OBJECT>";
	  document.write(WebSignClassID);
  }
} else {
	isie = false;
	console.log('not IE');
}

	var jstr = '{"cmd":1}';
	var ret = '';
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:"http://127.0.0.1:8173/crypto",    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				if(result.status == 1) throw new Error("请插入Ukey");
			}else{
				console.log("错误");
			}
		},
		error: function(xhr, type,errorThrown) { 
			console.log(xhr.status);
			console.log(xhr.readyState);
			console.log("失败");
			console.log(type);
			console.log(xhr);
			console.log(errorThrown);
			ret = '失败';
			if(xhr.readyState == 0) downloadFile("CNCryptoServer(srv3).exe");
		},
		complete: function(XMLHttpRequest, textStatus) {
			console.log(this); // 调用本次AJAX请求时传递的options参数
		}
	});
	 */
} catch (e) {    
	alert(e.message);
	throw e;
  }
function sn(usercode, isca) {
console.log(' sn(usercode, isca)' );
console.log(' sn usercode:' + usercode );
console.log(' sn isca:' + isca );
	var result = '';
	uscode = usercode;
	if (!isca) {
		if (signType == 'SOFT') {
			return usercode;
		} else {
			//ca根据type判断，type为本文件配置
			throw new Error('需要CA认证，但是未安装相应代码');
		}
	} else {
		//硬KEY
		if(isie)
		{
			var t = unisign.GetSNAndNCID();//ocx.getSNAndNCid();
			console.log("getCNAndNcid "+t);
			result = t.split(',')[0];
			ucode = t.split(',')[1];
			if(usercode != ucode) throw new Error('用户标识不一致');
			console.log(' result 16:' + result);
			result = ''+BigInteger.parseInt(result,16);
		}
		else
		{
			//google chrome
				var json = {};
				var json1={};
				json1.pwd="123456";
				json1.msg="123";
				json.cmd=0;
				json.params=json1;
				//var str=prompt("请输入密码","");
			var jstr = '{"cmd":1}';
			var ret = '';
			jQuery.support.cors = true;
			$.ajax({
                type:"post",    // 请求类型
                url:"http://127.0.0.1:8173/crypto",    // 请求URL
                data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
                dataType : 'json',  
				crossDomain: true, 
                cache:false, // 是否缓存
                async:false,    // 默认为true 异步请求
                success:function(result){    // 成功返回的结果(响应)
                    //console.info(result);
                    if(result){
                        console.log(result);
						ret = result;
						if(result.status == 1) throw new Error("请插入Ukey");
                    }else{
                        console.log("错误");
                    }
                },
				error: function(xhr, type) { 
					console.log("失败");
					console.log(type);
					console.log(xhr);
					ret = '失败';
				}
            });
			
			console.log('after ajax sync');
			//alert('after ajax sync');
			t = ret.response.sn;
			result = t.split(',')[0];
			ucode = t.split(',')[1];
			if(usercode != ucode) throw new Error('用户标识不一致');
			console.log(' result 16:' + result);
			result = ''+BigInteger.parseInt(result,16);
		}
	}
	console.log('sn(usercode, isca) result:' + result);
	return result;
}
function sign(value, uid, needpsw, isca) {
console.log(' sign(value, uid, needpsw, isca)' );
console.log(' sing needpsw:' + needpsw);
console.log(' sing value:' + value);
console.log(' sing uid:' + uid);
console.log(' sing isca:' + isca);
	var result = '';
	if (!isca) {
		if (signType == 'SOFT') {
			return CryptoJS.enc.Base64.stringify(CryptoJS.SHA256(uid + value));
		} else {
			//ca根据type判断，type为本文件配置
			throw new Error('需要CA认证，但是未安装相应代码');
		}
	} else {
		//硬KEY
		if(isie)
		{
			try{
				var selcsp = "EnterSafe ePass3000GM CSP v1.0";
				
				var base1 = new ESBase64();
				var db64strData = base1.encode(value)
				var cert = unisign.GetCert();
				signtxt = unisign.Sign(selcsp ,cert, db64strData);
				var trimst = iGetInnerText(signtxt); 
				var base = new ESBase64();
				var db64signtxt = base.encode(trimst)
				result = db64signtxt
			}catch (e) {
			 throw new Error(e.name + ": " + e.message);
			 //throw new Error('需要cA认证,但是未安装相应代码');
			}
		}else
		{
			if(needpsw)
			{
				//var jstr = '{"cmd":"sn","params":{"text":"sdfgrrbdffge"}}';
				var json = {};
					var json1={};
					json1.pwd="123456";
					json1.msg="123";
					json.cmd=0;
					json.params=json1;
					var str='';//prompt("请输入密码","");
					console.log(' sing value:' + value);
					var base1 = new ESBase64();
					var db64strData = base1.encode(value)
				var jstr = '{"cmd":0,"params":{"pwd":"'+str+'","msg":"'+value+'"}}';
				var ret = '';
				jQuery.support.cors = true;
				$.ajax({
					type:"post",    // 请求类型
					url:"http://127.0.0.1:8173/crypto",    // 请求URL
					data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
					dataType : 'json',  
					crossDomain: true, 
					cache:false, // 是否缓存
					async:false,    // 默认为true 异步请求
					success:function(result){    // 成功返回的结果(响应)
						//console.info(result);
						if(result){
							console.log(result);
							ret = result;
							if(result.status == 5) throw new Error("U盾密码不正确");
						}else{
							console.log("错误");
						}
					},
					error: function(xhr, type) { 
						console.log("失败");
						console.log(type);
						console.log(xhr);
						throw new Error("失败:" + " 类型：" + type + " 信息：" + xhr);
					}
				});
				
				console.log('after ajax sync');
				//alert('after ajax sync');
				try {
					var signtxt = ret.response.sig
					console.log('signtxt:'+ signtxt);
					var trimst = iGetInnerText(signtxt); 
					var base = new ESBase64();
					var db64signtxt = base.encode(trimst)
					result = db64signtxt
				}catch (e) {
				 throw new Error("请插入Ukey");
				 //throw new Error('需要cA认证,但是未安装相应代码');
				}
			}
			else
			{
								//var jstr = '{"cmd":"sn","params":{"text":"sdfgrrbdffge"}}';
				var json = {};
					var json1={};
					json1.pwd="123456";
					json1.msg="123";
					json.cmd=0;
					json.params=json1;
					var str='';//prompt("请输入密码","");
					console.log(' sing value:' + value);
					var base1 = new ESBase64();
					var db64strData = base1.encode(value)
				var jstr = '{"cmd":0,"params":{"pwd":"'+str+'","msg":"'+value+'","verify":0}}';
				var ret = '';
				jQuery.support.cors = true;
				$.ajax({
					type:"post",    // 请求类型
					url:"http://127.0.0.1:8173/crypto",    // 请求URL
					data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
					dataType : 'json',  
					crossDomain: true, 
					cache:false, // 是否缓存
					async:false,    // 默认为true 异步请求
					success:function(result){    // 成功返回的结果(响应)
						//console.info(result);
						if(result){
							console.log(result);
							ret = result;
							if(result.status == 5) throw new Error("U盾密码不正确");
						}else{
							console.log("错误");
						}
					},
					error: function(xhr, type) { 
						console.log("失败");
						console.log(type);
						console.log(xhr);
						throw new Error("失败:" + " 类型：" + type + " 信息：" + xhr);
					}
				});
				
				console.log('after ajax sync');
				//alert('after ajax sync');
				try {
					var signtxt = ret.response.sig
					console.log('signtxt:'+ signtxt);
					var trimst = iGetInnerText(signtxt); 
					var base = new ESBase64();
					var db64signtxt = base.encode(trimst)
					result = db64signtxt
				}catch (e) {
				 throw new Error("请插入Ukey");
				 //throw new Error('需要cA认证,但是未安装相应代码');
				}
			}
		}
		//throw new Error('需要CA认证，但是未安装相应代码');
		sn(uid, true);
	}
	console.log(' sign(value, uid, needpsw, isca) result:' + result );
	return result;
}
function superSign(value, usercode, needpsw, isca) {
console.log(' superSign value:' + value );
console.log(' superSign usercode:' + usercode );
console.log(' superSign needpsw:' + needpsw );
console.log(' superSign isca:' + isca );
	var result = {};
	try {
		result.status = 0;
		result.sn = sn(usercode, isca);
		result.signStr = sign(value, usercode, needpsw, isca);
	} catch (e) {
		result.status = 1;
		result.msg = e.message;
	}
	console.log(' superSign(value, usercode, needpsw, isca) result:' + result );
	return result;
}
function transSn(usercode) {
console.log(' transSn usercode:' + usercode );
	var result = '';
	if (transType == 'SOFT') {
		result = usercode;
	} else {
		//ca根据type判断，type为本文件配置
		result = sn(usercode, true);
	}
	console.log(' transSn result:' + result );
	return result;
}
//传输签名一定是不需要密码的
function transSign(value, uid) {
console.log(' transSign value:' + value );
console.log(' transSign uid:' + uid );
	var result = '';
	if (transType == 'SOFT') {
		result = CryptoJS.enc.Base64.stringify(CryptoJS.SHA256(uid + value));
	} else {
		//ca根据type判断，type为本文件配置
		result = sign(value, uid,false, true);
	}
	console.log(' transSign result:' + result );
	return result;
}



function superIsUsbKeyExist(usercode)
{
	console.log(' superIsUsbKeyExist(usercode)' );
	console.log(' sn usercode:' + usercode );
	let rslt = {}; //result.status 0成功1失败     result.msg失败的提示信息
	rslt.status = 1;
	rslt.msg = '';
	uscode = usercode;
	//google chrome
	let json = {};
	let json1={};
	json1.pwd="123456";
	json1.msg="123";
	json.cmd=0;
	json.params=json1;
	//var str=prompt("请输入密码","");
	let jstr = '{"cmd":1}';
	let ret = '';
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					return rslt;
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');
					let t = ret.response.sn;
					result = t.split(',')[0];
					ucode = t.split(',')[1];
					if(usercode != ucode) rslt.msg = '用户标识不一致';
					else rslt.status = 0;
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					return rslt;
				}
			}else{
				console.log("错误");
			}
		},
		error: function(xhr, type) { 
			console.log("失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
		}
	});
	return rslt;
}

/**获取证书信息
 *@param cert证书对象
 *@param field要获取的证书属性字段
 *@return 相应属性的值，如果输入了不支持属性，返回null
 */
function getCertField(cert,field)
{
	console.log(' getCertField(cert,field)' );
	console.log(' getCertField cert:' + cert );
	console.log(' getCertField field:' + field );
	let rslt = {}; //result.status 0成功1失败     result.msg失败的提示信息
	rslt.status = 1;
	rslt.msg = '';
	//{"cmd":25,"params":{"field":"xxxx",”cert”:xxxxxx}}
	let jstr = '{"cmd":25,"params":{"field":'+field+',"cert":"'+cert+'"}}';
	let ret = '';
	let retval = null;
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					throw new Error("请插入Ukey");
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');
					let t = ret.response.out;
					console.log('t:' + t);
					retval = t;
				}
			}else{
				console.log("错误");
				throw new Error("异常错误");
			}
		},
		error: function(xhr, type) { 
			console.log("失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
			throw new Error("异常错误:"+xhr);
		}
	});
	return retval;
}

/**签名
 * @param plainType 明文的类型，可以为 base64,hex,image,file,
 * @param signedType 输出的签名值的类型，可以为base64,hex
 * @param detached 当为false时，签名数据将会含有原文，当为true时，签名数据没有原文
 * @param plainData 代签名数据，当plainType为image时，该项为图片的url;
 * @param cert 签名使用的私钥对应的证书
 * @return 签名后的数据，签名失败返回null
 */
function SignMessage(plainType,signedType,detached,plainData,cert)
{
	console.log(' SignMessage(plainType,signedType,detached,plainData,cert)' );
	console.log(' SignMessage plainType:' + plainType );
	console.log(' SignMessage signedType:' + signedType );
	console.log(' SignMessage detached:' + detached );
	console.log(' SignMessage plainData:' + plainData );
	console.log(' SignMessage cert:' + cert );
	let rslt = {}; //result.status 0成功1失败     result.msg失败的提示信息
	rslt.status = 1;
	rslt.msg = '';
	//{"cmd":23,"params":{"cert":"xxxx",”in”:”xxxxx”,"detached":true,”signedType”:1,”msgType”:0}}
	let jstr = '{"cmd":23,"params":{"cert":"'+cert+'","in":"'+plainData+'","detached":'+detached+',"signedType":'+signedType+',"msgType":'+plainType+'}}';
	let ret = '';
	let retval = null;
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					throw new Error("请插入Ukey");
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');
					let t = ret.response.sig;
					console.log('t:' + t);
					retval = t;
				}
			}else{
				console.log("错误");
				throw new Error("异常错误");
			}
		},
		error: function(xhr, type) { 
			console.log("失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
			throw new Error("异常错误"+xhr);
		}
	});
	return retval;
}

/**验证签名
 * @param msgType 明文的类型，可以为 base64,hex,image,file,
 * @param signedType 输出的签名值的类型，可以为base64,hex
 * @param detached 当为false时，签名数据里含有原文，当为true时，签名数据里没有原文
 * @param plainData 代签名数据，当plainType为image时，该项为图片的url;
 * @param signedData 签名后的数据
 * @return 验签结果，验签成功返回对应证书，验签失败，返回null;
 */
function VerifySignature(msgType,signedType,detached,plainData,signedData)
{
	console.log(' VerifySignature(msgType,signedType,detached,plainData,signedData)' );
	console.log(' VerifySignature msgType:' + msgType );
	console.log(' VerifySignature signedType:' + signedType );
	console.log(' VerifySignature detached:' + detached );
	console.log(' VerifySignature plainData:' + plainData );
	console.log(' VerifySignature signedData:' + signedData );
	let rslt = {}; //result.status 0成功1失败     result.msg失败的提示信息
	rslt.status = 1;
	rslt.msg = '';
	//{"cmd":24,"params":{"sig":"xxxx",”in”:”xxxxx”,"detached":true,”signedType”:1,”msgType”:0}}
	let jstr = '{"cmd":24,"params":{"sig":"'+signedData+'","in":"'+plainData+'","detached":'+detached+',"signedType":'+signedType+',"msgType":'+msgType+'}}';
	let ret = '';
	let retval = null;
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					throw new Error("请插入Ukey");
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');
					let t = ret.response.cert;
					console.log('t:' + t);
					retval = t;
				}
			}else{
				console.log("错误");
				throw new Error("异常错误");
			}
		},
		error: function(xhr, type) { 
			console.log("失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
			throw new Error("异常错误"+xhr);
		}
	});
	return retval;
}

/**删除证书
 * @param cert 要删除的证书
 * @return void;
 */
function removeCert(cert)
{
	console.log(' removeCert(cert)' );
	console.log(' removeCert cert:' + cert);
	jQuery.support.cors = true;
	let rslt = {}; 
	let tokensn;
	let consn;
	let jstr = "";
	//{"cmd":26,"params":{"cert":"xxxx","verify":1}}
	jstr = '{"cmd":26,"params":{"cert":"'+cert+'","verify":1}}';
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					throw new Error("请插入Ukey");
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');					result = t.split(',')[0];
					let t = ret.status;
					console.log('删除证书:' + t);
				}
			}else{
				console.log("删除证书错误");
				throw new Error("删除证书错误");
			}
		},
		error: function(xhr, type) { 
			console.log("删除证书失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
			throw new Error("删除证书异常:"+xhr);
		}
	});	
//	//获取U盾序列号 {"cmd":16,"params":{"csp":"epass"}}
//	//获取容器序列号 {"cmd":9,"params":{"csp":"123456","sn":"1B27222007150716","inCert":"Base64Cert"}}
//	//删除容器 {"cmd":15,"params":{"tokenSn":"5036151203070918","conName":"459D342D-EAB3-4113-8ACE-11D3DF1F4533"}}
//	jstr = '{"cmd":16,"params":{"csp":"epass"}}';
//	$.ajax({
//		type:"post",    // 请求类型
//		url:qturl,    // 请求URL
//		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
//		dataType : 'json',  
//		crossDomain: true, 
//		cache:false, // 是否缓存
//		async:false,    // 默认为true 异步请求
//		success:function(result){    // 成功返回的结果(响应)
//			//console.info(result);
//			if(result){
//				console.log(result);
//				ret = result;
//				console.log("result.status:"+result.status);
//				if(result.status == 1) 
//				{
//					rslt.msg = "请插入Ukey";
//					console.log('rslt.msg:' + rslt.msg);
//					console.log('rslt.status:' + rslt.status);
//					throw new Error("请插入Ukey");
//				}
//				else
//				{
//					console.log('after ajax sync');
//					//alert('after ajax sync');					result = t.split(',')[0];
//					let t = ret.response.tokenSn.split(',')[1];
//					console.log('tokensn:' + t);
//					tokensn = t;
//				}
//			}else{
//				console.log("获取tokensn错误");
//				throw new Error("获取tokensn错误");
//			}
//		},
//		error: function(xhr, type) { 
//			console.log("获取tokensn失败");
//			console.log(type);
//			console.log(xhr);
//			ret = '失败';
//			throw new Error("获取tokensn异常:"+xhr);
//		}
//	});
//	
//	jstr = '{"cmd":9,"params":{"csp":"epass","sn":"'+tokensn+'","inCert":"'+cert+'"}}';
//	jQuery.support.cors = true;
//	$.ajax({
//		type:"post",    // 请求类型
//		url:qturl,    // 请求URL
//		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
//		dataType : 'json',  
//		crossDomain: true, 
//		cache:false, // 是否缓存
//		async:false,    // 默认为true 异步请求
//		success:function(result){    // 成功返回的结果(响应)
//			//console.info(result);
//			if(result){
//				console.log(result);
//				ret = result;
//				console.log("result.status:"+result.status);
//				if(result.status == 1) 
//				{
//					rslt.msg = "请插入Ukey";
//					console.log('rslt.msg:' + rslt.msg);
//					console.log('rslt.status:' + rslt.status);
//					throw new Error("请插入Ukey");
//				}
//				else
//				{
//					console.log('after ajax sync');
//					//alert('after ajax sync');
//					let t = ret.response.con;
//					console.log('conname:' + t);
//					consn = t;
//				}
//			}else{
//				console.log("获取conname错误");
//				throw new Error("获取conname错误");
//				
//			}
//		},
//		error: function(xhr, type) { 
//			console.log("获取conname失败");
//			console.log(type);
//			console.log(xhr);
//			ret = '失败';
//			throw new Error("获取conname失败"+xhr);
//		}
//	});
//	//删除容器 {"cmd":15,"params":{"tokenSn":"5036151203070918","conName":"459D342D-EAB3-4113-8ACE-11D3DF1F4533"}}
//	jstr = '{"cmd":15,"params":{"tokenSn":"'+tokensn+'","conName":"'+consn+'","verify":0}}';
//	console.log(jstr);
//	jQuery.support.cors = true;
//	$.ajax({
//		type:"post",    // 请求类型
//		url:qturl,    // 请求URL
//		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
//		dataType : 'json',  
//		crossDomain: true, 
//		cache:false, // 是否缓存
//		async:false,    // 默认为true 异步请求
//		success:function(result){    // 成功返回的结果(响应)
//			//console.info(result);
//			if(result){
//				console.log(result);
//				ret = result;
//				console.log("result.status:"+result.status);
//				if(result.status == 1) 
//				{
//					rslt.msg = "请插入Ukey";
//					console.log('rslt.msg:' + rslt.msg);
//					console.log('rslt.status:' + rslt.status);
//					throw new Error("请插入Ukey");
//				}
//				else
//				{
//					console.log('after ajax sync');
//					//alert('after ajax sync');
//					let t = result.status;
//					console.log('删除结果:' + t);
//				}
//			}else{
//				console.log("证书删除错误错误");
//				throw new Error("证书删除错误错误");
//				
//			}
//		},
//		error: function(xhr, type) { 
//			console.log("证书删除失败");
//			console.log(type);
//			console.log(xhr);
//			ret = '失败';
//			throw new Error("证书删除失败"+xhr);
//		}
//	});	
}



/**导出证书
 * @param 
 * @return cert (Base64encode);
 */
function exportCert()
{
	console.log(' exportCert' );
	let rslt = {}; //result.status 0成功1失败     result.msg失败的提示信息
	rslt.status = 1;
	rslt.msg = '';
	//{"cmd":25,"params":{"field":"xxxx",”cert”:xxxxxx}}
	let jstr = '{"cmd":19}';
	let ret = '';
	let retval = null;
	jQuery.support.cors = true;
	$.ajax({
		type:"post",    // 请求类型
		url:qturl,    // 请求URL
		data:jstr,    // 请求参数 即是 在Servlet中 request.getParement();可以得到的字符串
		dataType : 'json',  
		crossDomain: true, 
		cache:false, // 是否缓存
		async:false,    // 默认为true 异步请求
		success:function(result){    // 成功返回的结果(响应)
			//console.info(result);
			if(result){
				console.log(result);
				ret = result;
				console.log("result.status:"+result.status);
				if(result.status == 1) 
				{
					rslt.msg = "请插入Ukey";
					console.log('rslt.msg:' + rslt.msg);
					console.log('rslt.status:' + rslt.status);
					throw new Error("请插入Ukey");
				}
				else
				{
					console.log('after ajax sync');
					//alert('after ajax sync');
					let t = ret.response.cert;
					console.log('t:' + t);
					retval = t;
				}
			}else{
				console.log("错误");
				throw new Error("异常错误");
			}
		},
		error: function(xhr, type) { 
			console.log("失败");
			console.log(type);
			console.log(xhr);
			ret = '失败';
			throw new Error("异常错误:"+xhr);
		}
	});
	return retval;
}
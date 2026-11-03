/**
 * 组件：货币的常用js方法
 * 刘勇军
 * 2017年5月27日
 * 0.0.1 货币的常用js方法；
 */

bt.define(["jquery"],function(exports) {
	// "use strict";
	var $  = layui.jquery;
	var Money = function(){
		this.v = '0.0.1'; //版本号
	}
	Money.fn = Money.prototype;

	/* 把货币转换成数字
	* @param {string} obj 要转换的字符串 
	*/
	var toNumber = Money.fn.toNumber = function(obj){
		if(typeof obj === "number"){
			return obj;
		}else if(typeof obj === "string"){
			obj = obj.replace(/,/g, "");
			obj = obj.replace(/￥/g, "");
			if(!obj){
				return 0;
			}else if(isNaN(obj)){
				return 0;
			};
			return parseFloat(obj);
		}else{
			return 0;
		};
	}; //
	
	/** 
	  加法函数，用来得到精确的加法结果
	  @function
	  @param {Number} arg1 加数
	  @param {Number} arg2 加数
	*/
	Money.fn.add = function(){
		function doAdd(arg1, arg2){
			arg1 = toNumber(arg1);
			arg2 = toNumber(arg2);
			
			var r1,r2,m;
			try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
			try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
			m=Math.pow(10,Math.max(r1,r2));
			
			return (Math.round(arg1*m)+Math.round(arg2*m))/m;
		};
		
		var args = arguments;
		var sum = args[0] || 0;
		for(var i = 1; i < args.length; i++){
			sum = doAdd(sum, args[i]);
		};
		return Number(sum);
	}; // end add
	
	
	/** 
	  减法函数，用来得到精确的加法结果
	  @function
	  @param {Number} arg1 加数
	  @param {Number} arg2 加数
	*/
	Money.fn.sub = function(){
		function doSub(arg1, arg2){
			arg1 = toNumber(arg1);
			arg2 = toNumber(arg2)*-1;
			
			var r1,r2,m;
			try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
			try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
			m=Math.pow(10,Math.max(r1,r2));
			
			return (Math.round(arg1*m)+Math.round(arg2*m))/m;
		};
		
		var args = arguments;
		var sum = args[0] || 0;
		for(var i = 1; i < args.length; i++){
			sum = doSub(sum, args[i]);
		};
		return Number(sum);
	}; // end add
	
	/** 
	  乘法函数，用来得到精确的乘法结果
	  @function
	  @param {Number} arg1 被乘数
	  @param {Number} arg2 乘数
	  @param {Number} digits 有效数字
	*/
	Money.fn.mul = function(arg1,arg2,digits){
        arg1 = toNumber(arg1);
        arg2 = toNumber(arg2);
		var sum  = this.operation(arg1,arg2,digits,'multiply');
		sum = Number(sum);
		// var sum = this.format(sum,{ //　20200805 与 黄茜、郑刚沟通，决定取消格式化
		// 		separator:"",
		// 		decimal:digits
		// 	})
		return sum;
	}; // end mul
	/** 
	  除法函数，用来得到精确的除法结果
	  @function
	  @param {Number} arg1 被除数
	  @param {Number} arg2 除数
	  @param {Number} digits 有效数字
	*/
	Money.fn.div = function(arg1,arg2,digits){
        arg1 = toNumber(arg1);
        arg2 = toNumber(arg2);
		var sum  = this.operation(arg1,arg2,digits,'divide');
        sum = Number(sum);
		var sum = this.format(sum,{
				separator:"",
				decimal:digits
			})

		return sum;
	}; // end div
	
	//解决乘除运算的精度问题 开始
	/*
     * 判断obj是否为一个整数
     */
    function isInteger(obj) {
        return Math.floor(obj) === obj
    }
    
    /*
     * 将一个浮点数转成整数，返回整数和倍数。如 3.14 >> 314，倍数是 100
     * @param floatNum {number} 小数
     * @return {object}
     *   {times:100, num: 314}
     */
    function toInteger(floatNum) {
        var ret = {times: 1, num: 0}
        var isNegative = floatNum < 0
        if (isInteger(floatNum)) {
            ret.num = floatNum
            return ret
        }
        var strfi  = floatNum + ''
        var dotPos = strfi.indexOf('.')
        var len    = strfi.substr(dotPos+1).length
        var times  = Math.pow(10, len)
        var intNum = parseInt(Math.abs(floatNum) * times + 0.5, 10)
        ret.times  = times
        if (isNegative) {
            intNum = -intNum
        }
        ret.num = intNum
        return ret
    }
    
    /*
     * 核心方法，实现加减乘除运算，确保不丢失精度
     * 思路：把小数放大为整数（乘），进行算术运算，再缩小为小数（除）
     *
     * @param a {number} 运算数1
     * @param b {number} 运算数2
     * @param digits {number} 精度，保留的小数点数，比如 2, 即保留为两位小数
     * @param op {string} 运算类型，有加减乘除（add/subtract/multiply/divide）
     *
     */
    Money.fn.operation = function(a, b, digits, op) {
  
   //  	var b = this.format(b,{
			// 	separator:"",
			// 	decimal:digits
			// })
        var o1 = toInteger(a)
        var o2 = toInteger(b)
        var n1 = o1.num
        var n2 = o2.num
        var t1 = o1.times
        var t2 = o2.times
        var max = t1 > t2 ? t1 : t2
        var result = null;
        switch (op) {
            case 'add':
                if (t1 === t2) { // 两个小数位数相同
                    result = n1 + n2
                } else if (t1 > t2) { // o1 小数位 大于 o2
                    result = n1 + n2 * (t1 / t2)
                } else { // o1 小数位 小于 o2
                    result = n1 * (t2 / t1) + n2
                }
                return result / max
            case 'subtract':
                if (t1 === t2) {
                    result = n1 - n2
                } else if (t1 > t2) {
                    result = n1 - n2 * (t1 / t2)
                } else {
                    result = n1 * (t2 / t1) - n2
                }
                return result / max
            case 'multiply':
                result = (n1 * n2) / (t1 * t2)
                return result
            case 'divide':
                result = (n1 / n2) * (t2 / t1)
                return result
        }
    }

    //解决乘除运算的精度问题 结束


    /**
     将万元数值转换成元为单位(可以处理整数,小数,负数)
     @function
     @param n {Number|String} money 阿拉伯数字的金额
     @param moneyUnit {String} 默认为万元 million
     */
    Money.fn.convertMoney = function (n, moneyUnit) {
        if (moneyUnit === "yuan") {
            return this.toYuan(n);
        } else if (moneyUnit === "million") {
            return this.toMillion(n);
        } else {
            return this.unitConvert(n, moneyUnit, 2);
        }
    }

    /**
     将万元数值转换成元为单位(可以处理整数,小数,负数)
     @function
     @param n {Number|String} money 阿拉伯数字的金额
     */
    Money.fn.toYuan = function (n) {
        var money = toNumber(n);
        return this.mul(money, 10000);
    }; // end


    /**
     将数值转换万成元为单位(可以处理整数,小数,负数)
     @function
     @param n {Number|String} money 阿拉伯数字的金额
     */
    Money.fn.toMillion = function (n) {
        var money = toNumber(n);
        return this.div(money, 10000);
    }; // end

	/** 
	  将数值转换成中文金额(可以处理整数,小数,负数)
	  @function
	  @param {Number|String} money 阿拉伯数字的金额
	*/
	Money.fn.toChinese = function(n) {
		n = toNumber(n);
		
		var fraction = ['角', '分'];
		var digit = [  
			'零', '壹', '贰', '叁', '肆',  
			'伍', '陆', '柒', '捌', '玖'  
		];  
		var unit = [  
			['元', '万', '亿', "万亿"],  
			['', '拾', '佰', '仟']  
		];  
		var head = n < 0? '欠': '';  
		n = Math.abs(n);
		var s = '';  
		for (var i = 0; i < fraction.length; i++) {
			// s += (digit[Math.floor(n * 10 * Math.pow(10, i)) % 10] + fraction[i]).replace(/零./, ''); 
			s += (digit[Math.floor(this.mul(this.mul(n,10),Math.pow(10, i))) % 10] + fraction[i]).replace(/零./, '');
		};
		
		s = s || '整';  
		n = Math.floor(n);  
		for (var i = 0; i < unit[0].length && n > 0; i++) {  
			var p = '';  
			for (var j = 0; j < unit[1].length && n > 0; j++) {  
				p = digit[n % 10] + unit[1][j] + p;  
				n = Math.floor(n / 10);  
			}  
			s = p.replace(/(零.)*零$/, '')  
				 .replace(/^$/, '零')  
			  + unit[0][i] + s;  
		};
					    
		s = head + s.replace(/(零.)*零元/, '元')  
					   .replace(/(零.)+/g, '零')  
					   .replace(/^整$/, '零元整');  
		
		if(s.replace("万亿","").indexOf("亿") > -1){
			s = s.replace("万亿", "万");
		};
		
		return s;
	}; // end toChinese
	
	/** 
	  将中文金额转换成数值(可以处理中文金额 或 阿拉伯数字字符串)
	  @function
	  @param {String} money 中文金额
	*/
	Money.fn.toDigit = function(n){
		
		// Number
		if(typeof n == "number") return n;
		
		// 
		if(typeof n != "string") return null;
		
		// String
		if(!isNaN(n)) return parseFloat(n);
		
		// Chinese 
		var digit = [  
			'零', '壹', '贰', '叁', '肆',  
			'伍', '陆', '柒', '捌', '玖'  
		];
		var unit = ['万', '亿', '拾', '佰', '仟'];
		var fraction = ['角', '分', '整']; 
		var head = n.charAt(0) == "欠" ? -1 : 1;
		n = n.substr(1);
		
		var ary = n.split("元"),
			_int = ary[0],
			_dec = ary.length > 1 ? ary[1] : "";
		
		// 去除单位	
		for(var i = 0; i < unit.length; i++){
			_int = replace(_int, unit[i], "");
		};
		for(var i = 0; i < fraction.length; i++){
			_dec = replace(_dec, fraction[i], "");
		};
		
		// 替换数字
		for(var i = 0; i < digit.length; i++){
			_int = replace(_int, digit[i], i);
			_dec = replace(_dec, digit[i], i);
		};
		
		function replace(text, s1, s2){
			return text.replace(new RegExp(s1,"gm"), s2);;
		};
		
		return head * (parseFloat(_int + "." + _dec));
	}; // end toDigit
	
	/** 
	  格式化金额
	  @param {Number|String} num 要格式化数字
	  @param {Object} opt 参数集合，可选
	  @param {Int} opt.decimal 小数位数， 默认为2， 不足补0
	  @param {String} opt.separator 千位分隔符， 默认为逗号
	*/
	Money.fn.format = function(num, opt){
		num = toNumber(num);
		// opt = $.extend({
		// 	separator: ",",
		// 	decimal: 2
		// }, opt);
		if(opt){
			var separator = opt.separator;
			var decimal = opt.decimal;
			separator = separator === undefined ? ",":separator;
			decimal = decimal === undefined ? 2 : decimal;
			opt = {
				separator:separator,
				decimal:decimal
			}
		}else{
			var opt = {
				separator:",",
				decimal:2
			}
		}
		
		var re = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g,
			decimal = Math.pow(10,opt.decimal);
		num = this.add(num, 0);
		num = Math.round(num*decimal)/decimal;
		num = num.toString().replace(re,"$1" + opt.separator);
		
		// 如果小数位大于0
		if(opt.decimal > 0){
			num = num.split(".");
			
			if(num.length === 1){
				num.push("");
			};
			
			// 去除小数内的分隔符
			num[1] = num[1].replace(/,/g, "");
			
			// 补足小数位
			while(num[1].length < opt.decimal){
				num[1] += "0";
			};
			
			num = num.join(".");
		};
		
		return num
	};
	
	Money.fn.reFormat = function(ops){
		var text = $(".bt-biz-moneyType").text();
		var num = this.format(text,ops);
		$(".bt-biz-money").text(num)
	};
	/**
	 * 折算汇率格式化
	 * @param num
	 */
	Money.fn.formatConvert = function(num){
        var that = this;
        var opt = {
            separator: ",",
            decimal: bt.excRateConvert
        };
        num = that.format(num,opt);
        return num;
	};
	/**
	 * 资金计划汇率格式化
	 * @param num
	 */
	Money.fn.formatBudget = function(num){
		var that = this;
        var opt = {
            separator: ",",
            decimal: bt.excRateBudget
        };
        num = that.format(num,opt);
		return num;
	};
	/*
     Luhm校验规则：16位银行卡号（19位通用）:
	 
     1.将未带校验位的 15（或18）位卡号从右依次编号 1 到 15（18），位于奇数位号上的数字乘以 2。
     2.将奇位乘积的个十位全部相加，再加上所有偶数位上的数字。
     3.将加法和加上校验位能被 10 整除。
    */
	Money.fn.bankcard = function(bankNum){
		
        var regExp = /^\d+$/;
		
        if(bankNum.length == 0 || bankNum == null){
            return false;
        }else if(regExp.test(bankNum)== false){
            return false;
        }else{
            var lastNum = bankNum.substr(bankNum.length - 1,1),
				firstNum = bankNum.substr(0,bankNum.length - 1),
				array = firstNum.split(""),
				luhmSum = 0;
			
            for(var i = array.length - 1, j = 0; i >= 0; i--, j++){
                var k =parseInt(array[i]);
                if(j % 2 == 0)
                {
                    k = k*2;
                    if(k>10)
                        k = 1 + k % 10;
                    else if(k==10)
                        k = 1;
                }
                luhmSum += k;
            };
			
            var luhmNum =  (parseInt(luhmSum) % 10 == 0) ? 0:parseInt(10 - parseInt(luhmSum) % 10);
			
            if(lastNum != String(luhmNum)){
                return false;
            };
        };
		
		return true;
    }; // end bankcard
	
	/** 判断字符串是否身份证号 */
	Money.fn.idcard = function(idCard){
		
		var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子
		var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];				// 身份证验证位值.10代表X
		var sum = 0;
		var isValidityBrith = function(year,month,day){
			var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));
			if(year.length == 2){
				var temp_year = temp_date.getYear();
			}else if(year.length == 4){
				var temp_year = temp_date.getFullYear();
			}else{
				return false;
			}
			if(temp_year != parseFloat(year)   
				|| temp_date.getMonth() != parseFloat(month) - 1   
				|| temp_date.getDate() != parseFloat(day)){   
				return false;
			}else{
				return true;
			};
		};
		
		idCard = idCard.replace(/ /g, "").replace(/(^\s*)|(\s*$)/g, ""); 
		if(idCard.length == 15){
			var year =  idCard.substring(6,8),
				month = idCard.substring(8,10),
				day = idCard.substring(10,12); 
			
			return isValidityBrith(year,month,day);
		};
		
		if(idCard.length != 18) return false;
		
		var a_idCard = idCard.split("");
		if (a_idCard[17].toLowerCase() == 'x') a_idCard[17] = 10;
		for ( var i = 0; i < 17; i++) {
			sum += Wi[i] * a_idCard[i];
		};
		valCodePosition = sum % 11;			// 得到验证码所在位置
		if (a_idCard[17] != ValideCode[valCodePosition]) return false;
		
		var year =  idCard.substring(6,10),
			month = idCard.substring(10,12),
			day = idCard.substring(12,14);
		
		return isValidityBrith(year,month,day);
	}; // end idcard
	/**
	 * 元转换千元万元亿元
	 * @param num 被转换的数字
	 * @param moneyUnit 要转换成千元/万元/亿元
	 * @param digits 有效数字位数
	 * */
	Money.fn.unitConvert = function (num,moneyUnit,digits) { //thousand,tenThousand,billion
		var dividend;
		if(!moneyUnit){
			return toNumber(num);
		}
		var moneyUnit = moneyUnit.toLowerCase();
		if("yuan" == moneyUnit){
			return toNumber(num);
		}
		switch (moneyUnit){
			case 'thousand':
				dividend = 1000;
				break;
			case 'tenthousand':
				dividend = 10000;
				break;
			case 'billion':
				dividend = 100000000;
				break;
		}
		return this.div(num,dividend,digits)
	}


	var obj = {
		init:function(){
			var money = new Money();
			return money;
		}
	}
	var mMthod = function(){
		var money = new Money();
		return money;
	}
	
exports('money',mMthod())

});
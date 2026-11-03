/**
 * app组件：date常用js方法
 * 刘勇军
 * 2020年5月19日
 * 0.0.1 货币的常用js；
 */

	var DATE = {

		/**
		 尝试把对象（日期/数字/字符串）转换成日期对象，如果是日期则返回原值， 如果不能转换日期则返回null
		 @param {Date|Number|String} obj 要转换的对象
		*/
		get: function(obj){
			if(!obj){
				return null;
			}else if(obj.constructor == Date){
				return obj;
			}else if(obj.constructor == Number){
				var dt = new Date();
				dt.setTime(obj);
				return dt;
			}else if(obj.constructor == String){
				//var dt = new Date(obj.replace(/(-|\.)/g, "\/"));
				var newTime = obj.replace(/(-|\.)/g, "\/");
				if ( newTime.lastIndexOf("/") === 4 ){ //解决ie11 不支持 YYYY/MM 格式
                    newTime = newTime + '/01' //转变为YYYY/MM/DD格式
				}

              	var  mills = new Date(Date.parse(newTime)).getTime();//获得毫秒数

				var dt = new Date(mills);

				if(dt == "Invalid Date"){
					return null;
				}else if(dt == "NaN"){
					return null;
				};
				return dt;
			}else{
				return null;
			}
		},

		/**
		 把日期对象格式化成字符串
		 @param {Date|Number|String} date 要转换的日期对象，如果是数字或字符串会被自动转换成日期再格式化
		 @param {String} format 格式化的模板，默认为：yyyy-mm-dd
		*/
		// format: function(date, fmt) {

		// 	date = DATE.get(date);
		// 	if(!date) return "";
		// 	if(date.constructor != Date) return date;

		// 	var Re = /s{1,4}|m{1,4}|h{1,4}|d{1,4}|m{1,4}|yy(?:yy)?|"[^"]*"|'[^']*'/g;

		// 	var d = date.getDate(),
		// 		mm = date.getMonth(),
		// 		y = date.getFullYear(),
		// 		h = date.getHours(),
		// 		m = date.getMinutes(),
		// 		s = date.getSeconds(),
		// 		flags = {
		// 			s:    zeropad(s),
		// 			m:    zeropad(m),
		// 			h:    zeropad(h),
		// 			dd:   zeropad(d),
		// 			mm:   zeropad(mm + 1),
		// 			yy:   String(y).slice(2),
		// 			yyyy: y
		// 		};

		// 	fmt = fmt || "yyyy-mm-dd";
		// 	var ret = fmt.replace(Re, function ($0) {
		// 		return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
		// 	});

		// 	function zeropad(val, len) {
		// 		val = '' + val;
		// 		len = len || 2;
		// 		while (val.length < len) { val = "0" + val; }
		// 		return val;
		// 	}

		// 	// a small trick to handle special characters
		// 	return ret;
		// }, // end format
		/**
		 把日期对象格式化成字符串
		 @param {String} format 格式化的模板，默认为：yyyy-MM-dd ；时间分秒: yyyy-MM-dd hh:mm:ss
		*/
		format:function (date,fmt) {
			if(!date){
				return;
			}

			fmt = fmt || "yyyy-MM-dd";
			if(fmt.length > 8 && fmt.indexOf('hh') > 0){  //保证两种格式yy-mm-dd/yy-MM-dd 都没有问题
				fmt = 'yyyy-MM-dd hh:mm:ss';
			}else if(fmt.length === 8){
				fmt = "yyyyMMdd";
			}else{
				fmt = "yyyy-MM-dd";
			}
			if(date.length > 19){ //解决后台时间长度超过19的问题
            	date = date.substr(0,19);
        	}
			date = DATE.get(date);
			if(date == null){ return '';}
		    var o = {
		        "M+": date.getMonth() + 1, //月份
		        "d+": date.getDate(), //日
		        "h+": date.getHours(), //小时
		        "m+": date.getMinutes(), //分
		        "s+": date.getSeconds(), //秒
		        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
		        "S": date.getMilliseconds() //毫秒
		    };
		    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		    for (var k in o)
		        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
		    return fmt;
		},
		/*
		 比较两个时间的大小， 返回值是一个数字（时间转换为毫秒数之差）
		 @param {Date} dt1 要比较的时间
		 @param {Date} dt2 要比较的时间
		 @returns {Number|Null} 返回一个数字，等于null表示比较失败，等于0表示两时间相等，大于0表示第一个时间较大， 反之表示第一个时间较小
		*/
		compare: function(dt1, dt2){
			dt1 = DATE.get(dt1);
			dt2 = DATE.get(dt2);

			if(!dt1 || !dt2){
				return null;
			}else{
				return dt1.getTime() - dt2.getTime();
			};
		}, // end compare

		/**
	   * 得到两个日期之间的天数
	   * @param startDate
	   * @param endDate
	   */
		getBetweenDay:function(startDate,endDate){
		    var date1 = bt.date.get(startDate);
		    var date2 = bt.date.get(endDate);
		    var btt = date2 - date1;
		    return btt/(1000*60*60*24);
		},

		/**
	   * 得到两个日期之间的年数
	   * @param startDate
	   * @param endDate
	   */
		getBetweenYear:function(startDate,endDate){
		    var date1 = DATE.get(startDate);
		    var date2 = DATE.get(endDate);
		    var num = DATE.getBetweenMonth(startDate,endDate);
		    return Math.ceil(num/12);
		},

		/**
	   * 得到两个日期之间的月数
	   * @param startDate
	   * @param endDate
	   */
		getBetweenMonth:function(startDate,endDate){
		   var date1 = DATE.get(startDate);
		   var date2 = DATE.get(endDate);
		   var num = 0;

		    var o1 = {
		        "M+": date1.getMonth() + 1, //月份
		        "d+": date1.getDate(), //日
		        "h+": date1.getHours(), //小时
		        "m+": date1.getMinutes(), //分
		        "s+": date1.getSeconds(), //秒
		        "y+": date1.getFullYear(), //年
		    };
		    var o2 = {
		        "M+": date2.getMonth() + 1, //月份
		        "d+": date2.getDate(), //日
		        "h+": date2.getHours(), //小时
		        "m+": date2.getMinutes(), //分
		        "s+": date2.getSeconds(), //秒
		        "y+": date2.getFullYear(), //年
		    };
		    var yearCompare = o2["y+"] - o1["y+"];
		    var mothCompare = o2["M+"] - o1["M+"];
		    var dateCompare = o2["d+"] - o1["d+"];
		    // var hourCompare = o2["h+"] - o1["h+"];
		    // var minuteCompare = o2["m+"] - o1["m+"];
		    // var seecondCompare = o2["s+"] - o1["s+"];
		    if(yearCompare >= 0 ){
	    		if(dateCompare > 0){
	    			num = yearCompare * 12 + mothCompare + 1;
	    		}else {
	    			num = yearCompare * 12 + mothCompare;
	    		}
		    }else{
		    	num = yearCompare;
		    }
		    return num
		},

		/*
		 增加秒
		*/
		addSecond: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
			var d = date.getTime() + 1000*size;
			return new Date(d);
		},

		/*
		 增加分
		*/
		addMinute: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
			var d = date.getTime() + 60*1000*size;
			return new Date(d);
		},

		/*
		 增加时
		*/
		addHour: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
			var d = date.getTime() + 60*60*1000*size;
			return new Date(d);
		},

		/*
		 增加天
		*/
		addDay: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
			var d = date.getTime() + 86400000*size;
			return new Date(d);
		},

		/*
		 增加月
		*/
		addMonth: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
            size = size - 0; //转数字
			var m = date.getFullYear()*12 + date.getMonth() + size;
			return new Date(
				parseInt(m/12),
				Math.round(m%12),
				date.getDate(),
				date.getHours(),
				date.getMinutes(),
				date.getSeconds()
			);
		},

		/*
		 增加年
		*/
		addYear: function(date, size){
			date = DATE.get(date);
			if(!date){
				return null;
			};
            size = size - 0; //转数字
			return new Date(
				date.getFullYear() + size,
				date.getMonth(),
				date.getDate(),
				date.getHours(),
				date.getMinutes(),
				date.getSeconds()
			);
		},
		/*
		   毫秒数转为多少天多少小时多少秒多少毫秒
		 */
        getDate: function(ms){
        	if(!ms || !/^\d+$/.test(ms)){
        		return;
			}
			var obj = {};
            var ss = 1000;
            var mi = ss * 60;
            var hh = mi * 60;
            var dd = hh * 24;

            obj.days = parseInt( ms / dd );
            obj.hours = parseInt( (ms - obj.days * dd) / hh);
            obj.minutes = parseInt( (ms - obj.days * dd - obj.hours * hh) / mi);
            obj.seconds = parseInt( (ms - obj.days * dd - obj.hours * hh - obj.minutes * mi) / ss);
            obj.milliSecond =parseInt(  ms - obj.days * dd - obj.hours * hh - obj.minutes * mi - obj.seconds * ss);
			return obj
        },

		/*
		 毫秒数转为多少天多少小时多少秒 输入字符串
		 */
        getDateStr: function(ms){
            if(!ms || !/^\d+$/.test(ms)){
                return;
            }
            var obj = DATE.getDate(ms);

			var str = '';
			if(obj.days){
                str += obj.days +'天';
			}

            if(obj.hours ){
                str += obj.hours +'小时';
            }else{
            	if(str){
                    str += obj.hours +'小时';
				}
			}

            if( obj.minutes ){
                str += obj.minutes +'分钟';
            }else{
                if(str){
                    str += obj.minutes +'分钟';
                }
            }

            if(obj.seconds ){
                str += obj.seconds +'秒';
            }else{
                if(str){
                    str += obj.seconds +'秒 ';
                }
            }

           return str;

        }

	}; // end DATE

if(window.bt) {
	window.bt.date = DATE;
} else {
	window.bt = {
		date:DATE
	}
}

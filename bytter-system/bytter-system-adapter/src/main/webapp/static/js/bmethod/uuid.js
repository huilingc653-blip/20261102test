/**
 * 组件：uuid的js方法
 * zhengg
 * 2019年11月13日
 * 0.0.1 UUID的js方法；
 */

bt.define(function(exports) {
	var uuid = {

		/**
		 * 生成UUID
		 * @param len 长度
		 * @param radix 基数 2进制 8进制 10进制 16进制
		 * @returns uuid
		 * 调用：var uuidstr = uuid.random(32,16);//生成32位长度的基数为16进制的uuid
		 *  7FEA14A4722E273EE28C3F72E9E9141F
		 */
		random: function(len,radix){
			var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
		     var uuid = [], i;
		     radix = radix || chars.length;
		  
		     if (len) {
		       for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
		     } else {
		       var r;
		       uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		       uuid[14] = '4';
		       for (i = 0; i < 36; i++) {
		         if (!uuid[i]) {
		           r = 0 | Math.random()*16;
		           uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
		         }
		       }
		     }
		     return uuid.join('');
		},
		/**
		 * 生成UUID 生成32位长度的基数为16进制的uuid
		 *  7FEA14A4722E273EE28C3F72E9E9141F
		 */
		get: function(){
			return uuid.random(32,16);
		}
	};
exports('uuid',uuid)
});
bt.use(['jquery','form','btdate'], function(){
    $ = bt.jquery,form = bt.form();
    
    //时间
    $.each($("div[type='date']"),function(i,item){
		bt.btdate.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
			format: "YYYY-MM-DD",
			istime: true,
			placeholder: $(item).attr("defaultVal")
		});
	});
});
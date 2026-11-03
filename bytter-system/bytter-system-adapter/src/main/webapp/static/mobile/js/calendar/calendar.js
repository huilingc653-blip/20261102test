
var Calendar = function(){
    this.config = {
        elem:'',
        type:'month',
        max:'',
        min:'2000',
        value:'',
        change:null
    }
    this.randomNumber ='';
    this.ACTIVE ='active';
}
Calendar.fn = Calendar.prototype;

Calendar.fn.set = function(ops){
    $.extend(true, this.config, ops);
    if(!this.config.value){
        this.config.value = new Date();
    }
    this.randomNum();
}

Calendar.fn.init = function(ops){
    this.set(ops)
    var config = this.config
    if(config.type !=='month' || !config.elem ){
        return
    }
    config.max =  new Date(config.max).getFullYear() || new Date().getFullYear();
    config.min =  new Date(config.min).getFullYear() || '2000';
    this.createHtml();
}

Calendar.fn.createHtml = function(){
    var html = '<section class="bt-popup-container">'+ this.createYear() + this.createMoth()+'</section>';
    html += '<div class="bt-popup-bottom"><button type="button"  class="bt-popup-reset" id="clear'+this.randomNumber+'">重置</button>';
    html += '<button type="button" class="bt-popup-submit" id="submit'+this.randomNumber+'">确定</button></div>'
    $(this.config.elem).html(html);
    this.on();
    if(!window.biz){
        window.biz = []
    }

}
//注册事件
Calendar.fn.on = function(){
    var that = this;
    $('#year'+this.randomNumber).find('div>div').on('click',function(){
        var $this = $(this);
        $this.siblings().removeClass(that.ACTIVE);
        $this.addClass(that.ACTIVE);
        return false;
    })
    $('#month'+this.randomNumber).find('div>div').on('click',function(){
        var $this = $(this);
        $this.siblings().removeClass(that.ACTIVE);
        $this.addClass(that.ACTIVE);
        return false;
    })

    $('#clear'+this.randomNumber).on('click',function(){
        $(that.config.elem).find('.'+that.ACTIVE).removeClass(that.ACTIVE);
        that.setValue(that.config.value);
        return false;
    })
    $('#submit'+this.randomNumber).on('click',function(){
        var year = $('#year'+that.randomNumber).find('.'+that.ACTIVE).data('value'); // 2020-1
        var month = $('#month'+that.randomNumber).find('.'+that.ACTIVE).data('value'); // 2020-1
        if(month < 10){
            month = '0'+month;
        }
        if(year && month && that.config.change){
            that.config.change(year +'-'+month)
        }
        $('.ui-actionsheet').removeClass('show');
        var linkId = $(this).parents('.ui-actionsheet').data('link');
        $('#'+linkId).parent().removeClass('active');
        return false;
    })

}

Calendar.fn.setValue = function(value){
    if(!value){
        return
    }
    var selectYear = new Date(value).getFullYear();
    var selectMonth = new Date(value).getMonth() + 1;
    $(this.config.elem).find(this.ACTIVE);
    $('#year'+this.randomNumber).find('[data-value="'+selectYear+'"]').addClass(this.ACTIVE);
    $('#month'+this.randomNumber).find('[data-value="'+selectMonth+'"]').addClass(this.ACTIVE);
}

Calendar.fn.randomNum = function(){
    var num = Math.random()*1000000;
    num = parseInt(num, 10);
    this.randomNumber = num;
    return num;
}
Calendar.fn.createYear = function(){
    var config = this.config;
    var yearLength = config.max - config.min + 1;
    if(yearLength < 0){
        return ''
    }
    var selectYear = '';

    if(config.value){
        selectYear = new Date(config.value).getFullYear();
    }
    var yearHtml = '<div class="bt-date-year" id="year'+this.randomNumber+'"><div>';

    for(var i = 0; i< yearLength;i++){
        var year = config.max - i;
        var className = (selectYear && selectYear == year)? this.ACTIVE:'';
        yearHtml +='<div data-value="'+year+'" class="'+className+'">'+ year +'年</div>';
    }
    yearHtml +='</div></div>'
    return yearHtml
}

Calendar.fn.createMoth = function(){
    var config = this.config;
    var selectMonth = '';
    if(config.value){
        selectMonth = new Date(config.value).getMonth() + 1;
    }
    var monthHtml ='<div class="bt-date-month" id="month'+this.randomNumber+'"><div>';

    for(var i = 12; i > 0;i--){

        var className = (selectMonth && selectMonth == i)? this.ACTIVE:'';
        monthHtml +='<div data-value="'+i+'" class="'+className+'">'+ i +'月</div>'
    }
    monthHtml +='</div></div>'
    return monthHtml
}

Calendar.fn.get = function(id){
    if(id){

    }
}

if(!window.bt){
    window.bt = {}
}
bt.calendar = {
    init:function (ops) {
        var calendar = new Calendar();
        calendar.init(ops);
        return calendar;
    }
};

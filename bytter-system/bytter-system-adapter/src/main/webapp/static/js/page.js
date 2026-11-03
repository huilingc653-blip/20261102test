/**
 * 页码组件
 * 本组件依赖layui jquery
 * liuyj
 * 2017年12月15日
 * 0.0.1 基本功能
 * 0.0.2 新增加record 参数，用于设置本页是否要显示条数信息
 * 0.0.3 新增加custom参数,用于自定义
 */
bt.define(['jquery'], function(exports){
    "use strict";
    var $ = bt.jquery;
    var Page = function(){
        this.config = {
            elem:'',//页码容器
            lengthMenu:[10,25,50,100],//当表格每页显示多少条数据数组
            currentPage:1,//当页页码
            // pages:20,//共总多少页,可以根据len与court比较计算出pages
            len:10,//默认表格每页显示多少条
            record:true,//显示多少条信息区域
            // custom:{  //自定义对象
            //     start:'',//自定义固定每页起始
            // }
            // count:'',//总共多少页
            // prev:'',//上一页
            // next:'',//下一页
            // first:'',//首页
            // last:''//尾页
            // jump:function(obj,first){ //点击时调转,obj包括页码对象，

            // }

        }
    };

    /*
        参数合并

    */
    Page.prototype.set = function(ops){
        var that = this;
        $.extend(true, that.config, ops);
        return that;
    };

    Page.prototype.init = function(){
        var that = this;
        that.appendHtml();//追加页码html;
    };

    //页码的html
    Page.prototype.appendHtml = function () {
        var that = this;
        var _config = that.config;
        var lengthMenu = _config.lengthMenu;//每页显示多少条信息，array
        var len = _config.len;
        var currentPage = _config.currentPage;
        var count =  _config.count;
        var pages =_config.pages;
        var custom = _config.custom;
        if(_config.record && !pages){
            pages =  Math.ceil( count / _config.len );

        }
        if( count == 0 ){
            pages = 1;
        }
        var hoverMouse = currentPage == 1 ? 'firstPage':'table-jumpPage';
        var pageRightMouse = currentPage == pages ? 'firstPage':'table-jumpPage';
        //页码html
        var wrapper = '';
        wrapper += '<div class="wrapper pl">';
        var html = '';
        html += '<span class="table-page">';
        html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-firstPage span fa fa-fw fa-step-backward spanLeft page pageLeft '+ hoverMouse+'" title="First Page" ></span>';
        html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-prevPage span fa fa-fw fa-backward spanLeft page pageLeft '+ hoverMouse+'" title="Previous Page"></span>';
        html += '<span class="text-explode">|</span>'
        html += '<span class="table-pageInput span"><span style="margin-right: 5px;">第</span>';
        html += '<input class="table-pageNumber" value="'+ currentPage +'" > of';
        html += '<span class="pd2 total" style="padding-left:4px">'+pages+'</span></span>';
        html += '<span class="text-explode">|</span>';
        html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-nextPage span fa fa-fw fa-forward page pageRight '+ pageRightMouse +'" title="Next Page"></span>';
        html += '<span unselectable="on" style="-moz-user-select:none;" onselectstart="return false;" class="table-lastPage span fa fa-fw fa-step-forward page pageRight '+ pageRightMouse +'" title="Last Page"></span>';

        //每页显示多少条选框框
        if(lengthMenu){
            html += '<select lay-ignore class="table-length">';
            for(var i = 0; i < lengthMenu.length; i++){
                if(len === lengthMenu [i]){
                    html += '<option selected>';
                }else{
                    html += '<option>';
                }
                html += lengthMenu[i] +'</option>';
            }
            html += '</select>';
        }
        html += '</span>';

        if(_config.record){ //根据配置，是否要显示多少条信息栏
            var start;
            if( custom && custom.start !== undefined){
                start = custom.start;
            }else {
                start = 1;
                start = ( currentPage - 1 )* len + 1;
            }
            var end = len;
            end = currentPage * len ;
            if(count < end){
                end = count;
            }
            if(!count){
                start = 0;
            }

            //数据条数html
            var recordHtml = '';
            recordHtml += '<div class="table-record fr">';
            recordHtml += '<span class="pd2">'+bt.lang("common.button.view")+'</span>';//查看
            recordHtml += '<span class="table-startRecord1">'+( start )+'</span>';
            recordHtml += '<span class="pd2">-</span>';
            recordHtml += '<span class="table-endRecord1">'+( end )+'</span>';
            recordHtml += '<span class="pd2">'+bt.lang("common.table.records")+',</span>';//条记录
            recordHtml += '<span class="pd2">'+bt.lang("common.table.total")+'</span>';//共
            recordHtml += '<span class="table-totalRecord1">'+count+'</span>';
            recordHtml += '<span class="pd2">'+bt.lang("sys.config.appPageSize.unit")+'</span>';//条
            recordHtml += '</div>';
            //页码字符串合并
            wrapper += recordHtml;
        }

        wrapper += html;
        wrapper += '</div>';
        $(_config.elem).find('.wrapper').remove();
        if(!$(_config.elem).find('.wrapper')[0]){
            $(_config.elem).append(wrapper);
            that.setPageInformationEvent();
        }
    }
    //表格底部页码初始化注册方法
    Page.prototype.setPageInformationEvent = function () {
        var that = this;
        var config = that.config;
        pageInput(config);//保证输入的是正整数
        jumpPageEnter(config); //注册回车跳转事件
        jumpPageClick(config);//注册点击跳转事件
        if(config.lengthMenu){ //有下拉框时，注册下拉事件
            clickLengthMenu(config);//注册点击下拉框事件
        }
    }

    //控制只能输入正整数
    function pageInput(config){
        $(config.elem).off('keyup','.wrapper .table-pageNumber').on('keyup','.wrapper .table-pageNumber',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}
        }).off('afterpaste').on('afterpaste',function(){
            if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')};
        })

    }
    //按回车键页面跳转
    function jumpPageEnter(config){
        $(config.elem).off('keydown','.table-pageNumber').on('keydown','.table-pageNumber',function(e){
            e = event || windw.event;
            if(e.keyCode ===13){
                var $this = $(this);
                var val = parseInt($this.val());
                if(val >=0 ){
                    if(config.jump && typeof( config.jump ) === 'function'){
                        var total = $this.siblings('.total').text();
                        if(val > total ){
                            val = total;
                            $this.val(val);
                        }
                        var obj = {
                            elem:$this,
                            curr:val,
                            len:$this.parent().siblings('select').val()
                        }
                        config.jump(obj);
                        if(config.custom && config.custom.start){

                        }else{
                            updateRecord(config,obj);//更新页码显示起止信息条数
                        }
                        // config.jump(obj);
                    }
                }
                $this.blur();
            }
            return;
        })
    }
    //翻页跳转
    function jumpPageClick(config) {
        $(config.elem).off('click','.wrapper .table-jumpPage').on('click','.wrapper .table-jumpPage',function () {
            var title = $(this).attr('title');
            var currentPage = $(this).siblings('.table-pageInput').find('input').val() - 0;
            var totalPage =  $(this).siblings('.table-pageInput').find('.total').text() - 0;
            var pageNumber = 0;
            var pages = config.pages;
            switch (title){
                case 'First Page':
                    pageNumber = 1;
                    break;
                case 'Previous Page':
                    pageNumber = currentPage - 1;
                    break;
                case 'Next Page':
                    if(currentPage < totalPage){
                        pageNumber = currentPage + 1;
                    }else{
                        pageNumber = currentPage;
                    }
                    break;
                case 'Last Page':
                    pageNumber = totalPage;
                    break;
                default:
                    break;
            }

            if(config.jump && typeof( config.jump ) === 'function'){
                var elem = $(this);
                var obj = {
                    elem:elem,
                    curr:pageNumber,
                    len:$(this).siblings('select').val()
                }

                config.jump(obj);
                $(this).siblings('.table-pageInput').find('input').val( pageNumber );
                updatePageStyle( $(this),pageNumber,pages );//更新首页码样式
                if(config.custom && config.custom.start){

                }else{
                    updateRecord(config,obj);//更新页码显示起止信息条数
                }
                // config.jump(obj);
            }
        })
    }
    //点击下拉框选择每页显示多少条数据
    function clickLengthMenu(config) {
        $(config.elem).off('change','.wrapper .table-length').on('change','.wrapper .table-length',function () {
            var value = $(this).val();
            if(config.jump && typeof( config.jump ) === 'function'){
                var obj = {
                    elem:$(this),
                    curr:1,
                    len:value
                }
                config.jump(obj);
                $(this).siblings('.table-pageInput').find('input').val( 1 );//重新选择时，回到第一列车
                updateRecord(config,obj);//更新表格下方显示起止信息条数
            }
        })
    }
    //更新页码显示起止信息条数
    function updateRecord(config,obj) {
        var elem = obj.elem;
        var curr = obj.curr;
        var pages = config.pages;
        var start = 1;
        var count = config.count;
        var end = 10;
        var wrapper = $(config.elem).find('.wrapper');
        if(config.record){
            if(config.custom && config.custom.start){
                start = config.custom.start;
            }else{
                var len = obj.len ? obj.len : 10;
                start = (curr - 1) * len + 1;
                end = curr* len ;
            }
            if(count < end){
                end = count;
            }
            if(pages === undefined){
                pages = Math.ceil(count / len);
            }
            wrapper.find('.table-pageInput .total ').text( pages ); //总页码
            elem.parents('.wrapper').find('.table-record .table-startRecord1').text( start );
            elem.parents('.wrapper').find('.table-record .table-endRecord1').text( end );
            elem.parents('.wrapper').find('.table-pageInput .table-pageNumber').text( pages );
        }
        updatePageStyle(elem,curr,pages );//更新首页码样式
    }
    //更新首页或前一页的样式
    function updatePageStyle(elem,number,pages) {
        var firstPage = elem.parents('.wrapper').find('.table-firstPage');
        elem.parents('.wrapper').find('.page').removeClass('table-jumpPage').addClass('firstPage');
        if( number == 1){ //页码为首页时，添加firstPage样式
           if(pages == number){
                return;
           }else{
                elem.parents('.wrapper').find('.pageRight').addClass('table-jumpPage').removeClass('firstPage');
           }
        }else{ //页码不为首页时，删除firstPage样式

            if( number < pages ){
                elem.parents('.wrapper').find('.page').addClass('table-jumpPage').removeClass('firstPage');
            }else{
                elem.parents('.wrapper').find('.pageLeft').addClass('table-jumpPage').removeClass('firstPage');
            }
        }
    }
    //重置总页码数及总条数 ops有两个参数:elem 分页的容器；count 总共多少信息;
    Page.prototype.render = function(ops){
        if(!ops){
            return;
        }
        var that = this;
        var count = ops.count;
        var wrapper = $( that.config.elem).find('.wrapper');
        var len = wrapper.find('.table-length').val();
        var inputValue = wrapper.find('.table-pageNumber').val();
        if( ops.currentPage ){
            inputValue = ops.currentPage;
            wrapper.find('.table-pageNumber').val( inputValue );

        }
        var pages = Math.ceil( count / len );
        var start = ( inputValue - 1 ) * len + 1 ;
        var end = inputValue * len;
        if(end > count ){
            end = count;
        }
        if( count == 0 ){
            count = 1;
            start = end = 0;
        }
        //刷新分页
        wrapper.find('.table-pageInput span').text( pages ); //总页码
        wrapper.find('.table-record .table-startRecord').text( start );//起始条数
        wrapper.find('.table-record .table-endRecord').text( end );//截止条数
        wrapper.find('.table-record .table-totalRecord').text( count );//总条数
        //更新样式
        updatePageStyle( wrapper.find('.table-pageNumber'),inputValue,pages);
    }
    //更新分页样式
    Page.prototype.updateStyle = function (elem,number,pages) {
        updatePageStyle(elem,number,pages);
    }

    var obj = {
        init:function(ops){
            var page = new Page();
            page.set(ops);
            page.init();
            return page;
        }

    };
    //输入口
    exports('page', obj);
}).addcss('../../../css/page.css','pagecss');

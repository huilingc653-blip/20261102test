/**
 * 组件：打印类型业务组件
 * 刘勇军
 * 2017年9月01日
 * 0.0.1 基础版本
 * 0.0.2 添加了打印样式
 * 0.0.3 针对datatable表格进行特殊处理
 * 0.0.4 打印treeTable时，需要引入treetable样式
 * 0.0.5 优化打印显示效果
 * 0.0.6 新增栅格系统的样式
 */

layui.define(['jquery', 'lodop','layer'], function(exports) {
    var $ = layui.jquery;

    var PrintType = function () {
        this.config ={
            elem:"",

        };
        this.bizName = "printType";
        this.currentObject = null;
    }

    //   /**
    //  * 方法入口，初始化
    //  */
    PrintType.prototype.init = function (){
        this.render();
    };

    PrintType.prototype.render = function () {
        var _this = this;
        $('.bt-biz-printType:not(.init)').each(function(){
            var elem = $(this),
                printAreaElem  = elem.data('printareaid'.toLowerCase());
            elem.off('click').on('click',function(){
                layer.msg(bt.lang('common.msg.printing'),{time: 800}); //正在打印中
                var printObj = bt.lodop.init();
                //打印类型为voucher时,代表凭证打印
                var printType = $(this).data('printtype');
                setTimeout(function () {
                    LODOP = printObj.lodop;//获取lodop打印对象
                    if (!LODOP) {
                        return;
                    }
                    var printElem = $('#'+printAreaElem);
                    var  style ='<link rel="stylesheet" href="' + basePath + '/static/plugins/layui/css/layui.css" media="all">';
                    //添加公共样式，解决不换行的问题
                    style +='<link rel="stylesheet" href="' + basePath + '/static/css/common.css" media="all">'
                    if(printElem.find('.treetable').length){
                        style += '<link rel="stylesheet" href="' + basePath + '/static/plugins/treeTable/css/jquery.treetable.css" media="all">';
                    }
                    // voucher打印类型为凭证时,printType中会调用凭证打印模式
                    if (printType && printType === 'voucher'){
                        LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Auto-Width");
                        LODOP.SET_PRINT_STYLEA(0, "FontName", "宋体");
                        LODOP.ADD_PRINT_HTM(10,10,"100%","100%", printStyle+'<body>'+ printElem.html()+'</body>');
                        LODOP.PREVIEW();
                        return;
                    }
                    //新增栅格表格的样式
                    style += '<style>table th,table td {  border:1px solid #000!important; height:34px;}table.no-inner-border{border:1px solid #000!important;} table.no-inner-border th, table.no-inner-border td {border:0px solid #000!important; height:34px;} table tbody td,table thead th,table tfoot td{word-break: keep-all;white-space:nowrap;} .noprint {display:none!important;} .width30 {width:29%;display:inline-block;}'
                        + ' .width40 {width:39%;display:inline-block;} .textCenter {text-align:center} .fontSize18 {font-size:18px;height: 30px;} .textRight{text-align:right;}'
                        + ' table.layui-table {margin-top:4px;border:1px solid #000!important;} table {margin:0 auto} table th,table td {border-width: 1px!important;} .break-all{word-break: break-all!important;white-space:normal;!important;}'
                        + ' .line_border > div:not(.border0),.line_head_border > div:not(.border0){ border-right: 1px solid #000!important;} .line_head_border, .line_border { border: 1px solid #000!important;} div.layui-table { border-bottom: 1px solid #000!important;} .line { border-left:1px solid #000!important;border-bottom:1px solid #000!important;} ' +
                        '.line > .line {border: none!important;} .table_grid {border-left:1px solid #000!important;border-right:1px solid #000!important} '
                        +'</style>';
                    // var allhtml = document.getElementById(printAreaElem).innerHTML;
                    var allhtml='';
                    if(printElem.find('.dataTables_wrapper').length){ //当打印区域有datatable时
                        var datatableHtml = '';
                        var cloneElem = printElem.clone();
                        datatableHtml += '<table class="layui-table"> ';
                        datatableHtml += '<thead>';
                        datatableHtml += printElem.find('.dataTables_scrollHead table thead').html();//获取表头
                        datatableHtml += '</thead>';
                        datatableHtml += '<tbody>';
                        datatableHtml += printElem.find('.dataTables_scrollBody table tbody').html();//获取表体
                        datatableHtml += '</tbody>';
                        datatableHtml += '</table>';
                        cloneElem.find('.dataTables_wrapper').html(datatableHtml);
                        allhtml= cloneElem.html();//获取html
                    }else{
                        //如果有冻结树表格时，要引入树表格样式
                        if(printElem.find('.l-grid').length){
                            style +='<style>table th,table td {height: 28px!important;padding-top: 0!important;padding-bottom: 0!important;}</style>'
                            if( window.printType && window.printType['printHtml'] ){
                                var cloneHtml = $("#"+printAreaElem).clone();
                                cloneHtml.find('.l-panel').before($('<table class="layui-table"> '+window.printType['printHtml']+'</table>'))
                                cloneHtml.find('.l-panel').remove();
                                allhtml = cloneHtml.html();
                            }
                        }else{
                            allhtml = document.getElementById(printAreaElem).innerHTML;
                        }
                    }
                    var html = '<!DOCTYPE html><html lang="en"><head>' + style + '</head><body>' + allhtml + '</body></html>';
                    if (LODOP.PRINT_INIT){
                        LODOP.PRINT_INIT("")
                    }
                    LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT", "Width:100%");
                    // LODOP.SET_PRINT_MODE("PRINT_PAGE_PERCENT","Auto-Width");
                    LODOP.ADD_PRINT_HTM(10,10,"100%","100%", html);
                    // LODOP.ADD_PRINT_HTML('9mm', '0mm', 'RightMargin:0mm', "BottomMargin:9mm", html);
                    //LODOP.SET_PRINT_PAGESIZE(2, 0, 0, "A4");
                    LODOP.PREVIEW();
                },800);
            })
        })
    };
    function throwError(msg) {
        throw new Error("[bt-biz-PrintType:" + msg);
    };

    var printType = new PrintType();
    var obj = {
        init:function(ops){
            printType.init(ops);
            return this;
        },
        get:function(bizName){
            var obj = $('input[data-biz-name="'+bizName+'"]');
            if(obj.length ===0){
                throw new Error("[bt-biz-PrintType:元素容器找不到，具体设置格式请参考文档API");
            }
            printType.currentObject = obj;
            return printType;
        }
    }
    exports('printType',obj);
})

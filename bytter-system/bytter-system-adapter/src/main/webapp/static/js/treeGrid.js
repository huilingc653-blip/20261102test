/**
 * 树表功能
 *
 * 周亮
 * 2017年7月25日
 * 0.0.1 树形表格基础功能实现
 */
layui.define(['jquery','gridExtend'],function(exports){
    var $  = layui.jquery,jQuery=layui.jqueryr;

    var TreeGrid = function () {
        this.config ={
            elem:undefined
        }
    }

    TreeGrid.prototype.v = '0.0.1';

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    TreeGrid.prototype.init = function (options){
        if (options.elem === undefined) {
            throwError('TreeGrid Error:请配置参数elem,此参数为jQuery选择器!');
        }
        var jqGridPager = options.elem.substring(1,options.elem.length) + "_jqGridPager";
        var jqGridPagerSelect = "#"+jqGridPager;
        var treeGridOption = {
            url: options.url,
            datatype: "json",
            treeGrid: true,
            gridview:true,
            iconSet: "fontAwesome",
            treeGridModel: "adjacency",
            height: options.height?options.height:$(window).height() - (options.disparityHeight?options.disparityHeight:180),//options.disparityHeight 差距高度
            ExpandColClick : true,
            //表格自适应父级容器的宽度
            autowidth: true,
            treedatatype:"json",
            treeReader : {
                parent_id_field: "parentId"
            },
            viewrecords: false,
            pgbuttons:false,
            cellEdit:true,
            // cellsubmit : 'remote',
            loadComplete:function(data){
                tableGridDom.setOriginalData(data);
                if($.isFunction(options.loadComplete)){
                    options.loadComplete(data);
                }
            }
        };
        if(options.mtype === "post"){
            treeGridOption.ajaxGridOptions={
                contentType: 'application/json'
            };
            treeGridOption.serializeGridData=function (data) {
                return JSON.stringify(data);
            };
        }
        if("bottom"===options.navPosition){
            treeGridOption.pager = jqGridPagerSelect;
        }else if("top"===options.navPosition){
            treeGridOption.toppager = jqGridPagerSelect;
        }else if("all"===options.navPosition){
            treeGridOption.pager = jqGridPagerSelect;
            treeGridOption.toppager = true;
        }

        if(options.colModel){
            for(var i=0;i<options.colModel.length;i++){
                var model = options.colModel[i];
                if(model && typeof(model.sortable)==="undefined"){
                    options.colModel[i].sortable = false;
                }
            }
        }
        $.extend(treeGridOption, options);

        var tableGridDom = $(options.elem);
        if(!tableGridDom.parent().hasClass("treeGridWrapper")){
            tableGridDom.wrap('<div class="treeGridWrapper">');
            tableGridDom.after('<div id="'+jqGridPager+'"></div>');
        }
        tableGridDom.grid(treeGridOption).grid("setGridWidth").grid('bindKeys')
            .navGrid(jqGridPagerSelect,{
                edit: $.isFunction(options.editFunc),
                add: $.isFunction(options.addFunc),
                del: $.isFunction(options.delFunc),
                search: false,
                refresh: true,
                view: false,
                cloneToTop:true,
                addfunc:function(){
                    if($.isFunction(options.addFunc)){
                        var rowId = tableGridDom.grid('getGridParam','selrow');
                        var rowData = tableGridDom.grid('getOriginalData',rowId);
                        options.addFunc(rowData||null);
                    }

                },
                editfunc:function(rowId){
                    if($.isFunction(options.editFunc)){
                        var rowData = tableGridDom.grid('getOriginalData',rowId);
                        options.editFunc(rowData);
                    }
                },
                delfunc:function(rowId){
                    if($.isFunction(options.delFunc)){
                        var rowData = tableGridDom.grid('getOriginalData',rowId);
                        options.delFunc(rowData);
                    }
                }
            });

        //添加树表全部收起，全部展开
        tableGridDom.grid("customButton",{
            title:bt.lang('common.open.all'),//"全部展开",
            buttonicon:"fa-expand",
            onClickButton: function(){
                tableGridDom.grid("expandAll");
            }
        })
            .grid("customButton",{
            title:bt.lang('common.close.all'),//"全部收起",
            buttonicon:"fa-compress",
            onClickButton: function(){tableGridDom.grid("collapseAll");}
        });



        $(window).unbind("resize").bind("resize", function () {
            var width = $(".treeGridWrapper").width();
            tableGridDom.setGridWidth(width);
        });

        return tableGridDom;
    };

    function throwError(msg) {
        throw new Error(msg);
    }

    var treeGrid = new TreeGrid();
    var obj = {
        init: function(option){
            return treeGrid.init(option);
        }
    };
    //输出test接口
    exports('treeGrid', obj);
});

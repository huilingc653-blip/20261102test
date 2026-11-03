/**
 * 联动组件
 * 本组件依赖jquery、ztreecore
 * 周亮
 * 2017年4月19日
 * 0.0.1 基础下拉树
 * 0.0.2 加入同一个页面加载多个下拉树不受影响
 * 0.0.3 加入单选，checkbox，radio下拉树
 * 0.0.4 加入隐藏域框
 * 0.0.5 加入传入已选项时填充输入框
 * 0.0.6 加入单选下拉树时，禁止选择子节点
 * 0.0.7 新增节点点击回调方法
 * 0.0.8 新增，获取参数完成后回调方法
 * 0.0.9 新增点击、选中节点反回当前节点对象或集合
 * 0.1.0 新增删除数据
 * 0.1.2 修改了radio赋值hiddenInput的值
 * 0.1.3 在datatable中，使用此控件，需要重新计算列宽
 * 0.1.4 修改reset方法，重置form中所有selectTree
 * 0.1.5 新增加initComplete 下拉树初始化完成之后回调方法
 * 0.1.6 修改reset方法，重置form中所有selectTree
 * 0.1.7 修改多选即为checkbox的时候，可以单选
 * 0.1.8 新增输入框点击回调方法，清除回调方法
 * 0.1.9 reset方法重写
 * 0.1.10 添加下拉树的定位
 * 0.1.11 添加下拉树自定义宽度设置 data-width属性
 * 0.1.12 添加下拉树title显示全部内容属性
 * 0.1.13 新增下拉树检索功能
 * 0.1.14 复选框点击文字可以不关闭下拉树，可以连续点击选择
 * 0.1.15 复选框点击选择中时，会添加字体颜色
 * 0.1.16 新增加textStyle参数，用于设置内容的样式（原因fontCss不知道为什么被注销了，所以重新启动另外一个参数)
 * 0.1.17 新增加valueField参数，默认值为id
 * 0.1.18 解决win10 ie11模糊搜索问题，将逻辑从失去焦点清空内容改成非选中关闭下拉树时清空内容
 * 0.1.19 解决ie浏览器点击单选框反映慢的问题
 * 0.1.20 优化点击文字选择时传回参数
 * 0.1.21 新增searchType搜索类型的标记参数，默认为搜索结果，显示所有的父级,参数为'corp'时，显示单位类型搜索结果
 */
layui.define(['jquery','ztree','ztreeCheck','layer','ztreeHide'],function(exports){
    var $  = layui.jquery;
    var SelectTree = function () {
        this.config ={
            url:undefined,
            elem:undefined,
            name:undefined,
            bizName:undefined,
            expandAll:true,
            checkType:undefined,
            valueField:'id',// 选中时，默认赋值id
            selected:'',
            currentNode:'',//该参数只对单选的下拉树有效，当传入该参数时，将会校验点击选择的节点是否为该节点的子节点。如果是，则禁止选择
            setting:{//该setting为ztree的setting，可参考http://www.treejs.cn/v3/api.php的API
                data:{
                    simpleData: {
                        enable: true
                    }
                }
            },

            //0.0.7新增
            clickCallBack:undefined,
            //0.0.9新增
            clickCallBackData:undefined,
            //2017-09-05 14:51
            inputClick:undefined,
            inputBeforeClick:undefined,
            clearCallback:undefined,

            //fontCss:undefined,
            //fontCss:{color:'red'},
            //textStyle:function(){}， //新增加内容设置样式的方法
            placeholder:undefined,
            separator:undefined,
            //0.0.8新增，重新渲染下拉树异步获取数据时的参数
            param:undefined,
            //function，0.0.8新增，获取参数完成后回调方法
            beforeInit:undefined,
            //0.1.0新增
            deleteFlag:false,
            required:false,
            beforeClick:undefined,
            //function，0.1.5新增，下拉树初始化完成之后回调方法
            initComplete:undefined,
            disabled:false,
            lazyLoad : false,
            searchType: undefined, // 新增搜索类型的标记参数，默认为搜索结果，显示所有的父级,参数为'corp'时，显示单位类型搜索结果
        }
        this.param = {
            treeContent:'',
            treeList:'',
            treeInput:'',
            treeHiddenInput:''
        }
        this.randomNumber = 0;
        this.hiddenTreeNodes = {
            treeList:'',
            hiddenNodes:[]
        };//用于存储被隐藏的结点
        this.treeListArr = [], // 一维数据，树的所有数据
        this.searchResultList = [] // 搜索结果清单
    }

    SelectTree.prototype.v = '0.1.9';

    SelectTree.prototype.init = function(options,isCorp){
        var that = this;

        if (options.url === undefined) {
            throwError('SelectTree Error:请配置远程数据URL!');
        }
        if (options.elem === undefined) {
            throwError('SelectTree Error:请配置参数elem,此参数为jQuery选择器!');
        }
        if (options.name === undefined) {
            throwError('SelectTree Error:请配置参数name!');
        }
        $.extend(true, that.config, options);

        var _config = that.config;

        that.initConfiguration(isCorp);

        //add by Zhangxb 2017.7.20 同时注释掉 102 行 $(_config.elem).detach();
        // that.removeContainer();
        //初始化多个元素的ID值，避免同一个页面出现多个下拉树产生事件冲突
        that.initContainerId();

        //0.0.7新增
        //初始保存树的配置
        if(!layui.selectTree.config){
            layui.selectTree.config  = [];
        }
        layui.selectTree.config[that.param.treeContent] = _config;

        //初始化放至ztree的容器
        that.initTreeContainer();
        that.initTree();


        //处理点击消失事件
        $(document).unbind('mousedown').bind("mousedown",that.hideSelectTreeEvent);
        $(_config.elem).detach();

        return that;
    }

    /**
     * 初始化树 0.0.8提取
     */
    SelectTree.prototype.initTree = function(){
        var that = this;
        var _config = this.config;
        //是否延迟加载
        if(_config.lazyLoad){
            _config.lazyLoad = false;
            return;
        }
        //初始化树
        $.get(_config.url,_config.param,function(nodes){
            // 如果是单位，我们将开启新的搜索功能
            if(_config.searchType === 'corp'){
                nodes = _config.beforeInit?_config.beforeInit(nodes):nodes;
                that.getTreeListArr(nodes,that)
            }else{
                nodes = _config.beforeInit?_config.beforeInit(nodes):nodes;
            }

            var ztree = $.fn.zTree.init($("#"+that.param.treeList), _config.setting, nodes);
            ztree.expandAll( _config.expandAll);
            if($(_config.elem).length === 0){
                $("#"+that.param.treeInput).val("");
                $("#"+that.param.treeHiddenInput).val("");
            }
            that.setTreeSelectStatus(ztree);
            //下拉树初始化完成回调方法
            if(_config.initComplete && typeof _config.initComplete ==='function'){
                _config.initComplete(ztree)
            }

            if($.fn.dataTable && $("#"+that.param.treeInput).parents('.dataTables_wrapper').length){ //表格中的有下拉树才重新计算
                $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
            }
            // 添加检索功能
            that.search(ztree)
        },'JSON');
    }
    /**
     * 获取树的一维数组数据
     */
    SelectTree.prototype.getTreeListArr = function(nodes){
        /**
         * 通过树型数据生成一个一维数据
         *
         */
        function creatList(nodes,selectTreeObj) {
            for (var i = 0; i < nodes.length; i++) {
                var node = nodes[i]
                selectTreeObj.treeListArr.push({
                    parentIds:node.parentIds,
                    id:node.id,
                    name:node.name
                })
                if(node.children && node.children.length){
                    creatList(node.children,selectTreeObj)
                }
            }
        }
        creatList(nodes,this)
    }
    SelectTree.prototype.initConfiguration = function(isCorp){
        var that = this;
        var _config = this.config;
        var setting = {};
        //若不配置checkType，则为单选
        if(typeof (_config.checkType) === "undefined"){
            setting.callback ={
                onClick : that.onClick,
                beforeClick:that.onBeforeClick
            }
        }else{
            setting.callback ={
                onCheck:that.onCheck,
                onClick : that.onClick,
                beforeClick:that.onBeforeClick
            }
            setting.check = {};
            setting.check.enable = true;
            //check为多选
            if(_config.checkType==="check"){
                setting.check.chkStyle = "checkbox";
                if(!_config.setting.check || !_config.setting.check.chkboxType){
                    setting.check.chkboxType = {"Y":"", "N":""};
                }
                //radio为单选，默认整棵树单选
            }else if(_config.checkType==="radio"){
                setting.check.chkStyle = "radio";
                if(!_config.setting.check || !_config.setting.check.radioType){
                    setting.check.radioType = "all";
                }
            }
        }

        //0.0.7新增
        //改变字体
        //if(typeof (_config.fontCss) !== "undefined"){
        //    _config.setting.view?"":_config.setting.view={};
        //    _config.setting.view.fontCss = _config.fontCss;
        //}

        //0.1.16新增
        //新增加设置文本样式
        if(typeof (_config.textStyle) !== "undefined"){
            _config.setting.view?"":_config.setting.view={};
            _config.setting.view.fontCss = _config.textStyle;
        }
        // if(isCorp){
        //     if(_config.setting.view){
        //         _config.setting.view.showIcon = false;
        //         _config.setting.view.showLine = false;
        //     }else{
        //         _config.setting.view = {
        //             showIcon:false,
        //             showLine:false
        //         }
        //     }
        // }

        $.extend(true, _config.setting, setting);
    }

    SelectTree.prototype.initContainerId = function(){
        var that = this;
        var number = that.randomNum();
        that.param.treeContent = 'treeContent'+number;
        that.param.treeList = 'treeList'+number;
        that.param.treeInput = 'treeInput'+number;
        that.param.treeHiddenInput = 'treeHiddenInput'+number;
    }

    //add by Zhangxb 2017.7.20
    SelectTree.prototype.removeContainer = function(){
        var that = this;
        var elems=$(that.config.elem).siblings();
        for(var i=0;i<elems.length;i++){
            $(elems[i]).remove();
        }
    }

    SelectTree.prototype.randomNum = function(){
        var num = Math.random()*1000000;
        num = parseInt(num, 10);
        this.randomNumber = num;
        return num;
    }

    SelectTree.prototype.initTreeContainer = function(){
        var that = this;
        var _config = that.config;
        var treeContainer = $(_config.elem);
        //初始化输入框
        var width = treeContainer.data('width');//重置下拉表格宽度

        //var input = $('<input id="'+that.param.treeInput+'" type="text" '+(_config.placeholder?'placeholder="'+_config.placeholder+'"':'')+' class="layui-input select-tree-input" '+ (width?'style="width:'+width+';" ':'') + (_config.required?' lay-verify="required" ':'') +' readonly />');
        var input = $('<input id="'+that.param.treeInput+'" type="text" '+(_config.bizName?'data-biz-name="'+_config.bizName+'"':'' )+(_config.placeholder?'placeholder="'+_config.placeholder+'"':'')+' class="layui-input select-tree-input'+ (_config.disabled?' layui-disabled':'')+'" '+ (width?'style="width:'+width+';" ':'') + (_config.required?' lay-verify="required" ':'') + (_config.disabled?' disabled ':'') +' />');

        //给指定标签下创建输入框
        treeContainer.before(input);

        that.addDelIcon(input);

        var hiddenInput = $('<input id="'+that.param.treeHiddenInput+'" type="hidden" name="'+_config.name+'" '+(_config.currentNode?'data-current="'+_config.currentNode+'"':'')+'  data-defaultvalue="'+_config.selected+'"/>');
        treeContainer.before(hiddenInput);

        //0.1.0新增删除数据
        if(_config.deleteFlag){
            input.css({'float':'left','width':'85%'});
            var deleteContent = $('<div style="float: left;height: 30px;line-height: 30px;text-align: center;border: 1px solid #e6e6e6;border-left: none;border-radius: 2px;width: 25px;cursor: pointer;"><i class="fa fa-close" aria-hidden="true"></i></div>');
            deleteContent.click(function(){
                input.val('');
                hiddenInput.val('');
                if(_config.checkType=="check" || _config.checkType=="radio"){
                    that.deleteSelectValue(that.param.treeList);
                }
            });

            treeContainer.after(deleteContent);
        }

        //给输入框加入获取焦点事件
        input.unbind();
        input.click(function(){
            if(_config.inputBeforeClick && !_config.inputBeforeClick()){
                return;
            }
            //先清除当前显示的下拉树
            that.hideSelectTree();
            //再显示下拉树
            that.showSelectTree();
        })

        // 0.1.14 新增：复选框时，点击文字选中时，清除选中的背影颜色
        var isCheck = _config.checkType =="check" ? "isCheck":"";

        var treeContent = $('<div id="'+that.param.treeContent+'" class="select-tree layui-anim layui-anim-upbit '+isCheck+'" '+ (width?'style="width:'+width+';" ':'')+'></div>');


        var treeUlContent = $('<ul id="'+that.param.treeList+'" class="ztree select-tree-list"></ul>');
        treeContent.append(treeUlContent);

        treeContainer.after(treeContent);

        //0.1.0新增表单重置事件监听
        // treeContent.parents('form').find('button[type=reset]').on('mousedown',function(){  liuyj 注销
        //     //重置form中所有的selecTree
        //     var treeContents =$(this).parents('form').find('.select-tree ');
        //     bt.each(treeContents,function () {
        //         $(this).siblings('input[type="hidden"]').val('');
        //         if(_config.checkType=="check" || _config.checkType=="radio"){
        //             var id = $(this).find('.select-tree-list').attr('id');
        //             that.deleteSelectValue(id);
        //         }
        //     });
        // });

        //0.1.0新增表单重置事件监听  liuyj
        treeContent.parents('form').find('button[type=reset]').on('mouseup',function(){
            //重置form中所有的selecTree
            var selected = hiddenInput.data('defaultvalue'.toLocaleLowerCase());
            setTimeout(function(){
                that.val(selected);
            }, 50);
        });
    };

    //下拉树检索功能
    SelectTree.prototype.search = function(ztree){
        var that = this;

        //查找不符合条件的叶子节点
        function filterFunc(node){
            var _keywords= $.trim( $('#'+ that.param.treeInput).val());
            var textArr = toArray( _keywords );
            var isSearch = true,len = textArr.length;

            if( len ){
                for(var i = 0;i< len;i++){
                    if( node.name.indexOf( textArr[i] ) ==-1 ){
                        isSearch = false;
                        break;
                    }
                }
            }
            if( node.isParent){
                if( len && isSearch ){
                    $("#" + node.tId).addClass('search_result');//设置检索
                }else{
                    $("#" + node.tId).removeClass('search_result');
                }
                return false;
            }
            if( isSearch ){
                if( len ){
                    $("#" + node.tId).addClass('search_result')
                }else{
                    $("#" + node.tId).removeClass('search_result');
                }
                return false;
            }

            $("#" + node.tId).removeClass('search_result');
            return true;
        };

        // 单位树的数据方法
        function filterFuncByCorp(node) {
            if(!that.searchResultList||that.searchResultList.length==0){
                $("#" + node.tId).removeClass('search_result');
                return false
            }
            var isSearch = that.searchResultList.indexOf(node.id) ===-1?false:true
            var len = that.searchResultList.length;
            if( node.isParent){
                if( len && isSearch ){
                    $("#" + node.tId).addClass('search_result');//设置检索
                    return false
                }else{
                    $("#" + node.tId).removeClass('search_result');
                }
                return true;
            }
            if( isSearch ){
                if( len ){
                    $("#" + node.tId).addClass('search_result')
                }else{
                    $("#" + node.tId).removeClass('search_result');
                }
                return false;
            }

            $("#" + node.tId).removeClass('search_result');
            return true
        }

        //　节流函数
        function throttle(fn, threshhold) {
            var timeout
            var start = new Date;
            var threshhold = threshhold || 0
            return function () {
                var context = this, args = arguments, curr = new Date() - 0
                clearTimeout(timeout)//总是干掉事件回调
                if(curr - start >= threshhold){
                    fn.apply(context, args) //只执行一部分方法，这些方法是在某个时间段内执行一次
                    start = curr
                }
            }
        }
        function getSearchResultList() {
            that.searchResultList = []
            var _keywords= $.trim( $('#'+ that.param.treeInput).val());
            var textArr = toArray( _keywords );
            var len = textArr.length;
            if(len){
                for (var j = 0; j < that.treeListArr.length; j++) {
                    var node = that.treeListArr[j]
                    var isResult = false // 是否找到了结果
                    for(var i = 0;i< len;i++){
                        if( node.name.indexOf( textArr[i] ) !=-1 ){
                            isResult = true
                            break;
                        }
                    }
                    if(isResult){
                        if(that.searchResultList.indexOf(node.id) === -1){
                            that.searchResultList.push(node.id)
                        }
                        var parentIds = node.parentIds.split(',')
                        if(parentIds && parentIds.length){
                            for (var i = 0; i < parentIds.length; i++) {
                                var pid = parentIds[i]
                                if(that.searchResultList.indexOf(pid) === -1){
                                    that.searchResultList.push(pid)
                                }
                            }
                        }

                    }


                }
            }
        }
        var searchSelectTree = throttle(function(treeList) {
            // 显示所有节点
            $( '#'+treeList ).find('.layui-hide').removeClass('layui-hide');
            ztree.showNodes(that.hiddenTreeNodes.hiddenNodes);
            //如果是单位，将开启新的搜索功能；
            if(that.config && that.config.searchType === 'corp'){
                // 获取搜索结果清单数据
                getSearchResultList()
                //获取不符合条件的叶子结点
                that.hiddenTreeNodes.hiddenNodes = ztree.getNodesByFilter(filterFuncByCorp);
            }else{
                //获取不符合条件的叶子结点
                that.hiddenTreeNodes.hiddenNodes = ztree.getNodesByFilter(filterFunc);
            }
            ztree.hideNodes(that.hiddenTreeNodes.hiddenNodes);
        });

        $('body').find( '#'+ that.param.treeInput).on('input propertychange',function(){
            var $this = $(this);
            //0.1.18 获取treeList方法更改，原方法错误
            //var treeList = $this.siblings('.select-tree > ul').attr('id');
            var treeList = $this.siblings('.select-tree').children('ul').attr('id');
            if( that.hiddenTreeNodes.treeList != treeList ){
                that.hiddenTreeNodes.treeList = treeList;
            }
            //执行搜索下拉树回调
            searchSelectTree(treeList);
            // 显示所有节点
            // $( '#'+treeList ).find('.layui-hide').removeClass('layui-hide');
            // ztree.showNodes(that.hiddenTreeNodes.hiddenNodes);
            // //获取不符合条件的叶子结点
            // that.hiddenTreeNodes.hiddenNodes = ztree.getNodesByFilter(filterFunc);
            // ztree.hideNodes(that.hiddenTreeNodes.hiddenNodes);
        })
            .on('focus',function(){
                //显示上次搜索后背隐藏的结点
                if (!$(this).val().length){ // 0.1.18 输入框有内容时，不显示树所有内容
                    $('#'+ that.param.treeList).find('.layui-hide').removeClass('layui-hide');
                    ztree.showNodes(that.hiddenTreeNodes.hiddenNodes);
                    that.hiddenTreeNodes.hiddenNodes = [];
                    $(this).siblings('.select-tree').find('.search_result').removeClass('search_result');
                }
            })
            .on('blur',function(){ //丢去焦点时
                var nodes = ztree.getSelectedNodes();//获取选中数据
                if(!nodes.length){ // 无选中数据时，直接清空
                    // 0.1.18 注销失去焦点清空内容方法
                    //  $(this).val("");
                    return;
                }else{
                    $(this).val($(this).attr('title'));//还原选中的name
                }
            })
    }
    // 字符串转数组
    var toArray = function(str){
        var arr = str.split(" ");
        for(var i = 0 ;i<arr.length;i++){
            if(arr[i] == "" || typeof(arr[i]) == "undefined")
            {
                arr.splice(i,1);
                i= i-1;
            }
        }
        return arr;
    }
    // 添加删除按钮事件
    SelectTree.prototype.addDelIcon = function(inputElem){
        var that = this;
        var _config = that.config;
        //var width = $(inputElem).width();
        //var icon = '<a href="javascript:void(0);"><i class="layui-icon bt-selectTree-close" style="left:'+(width-7)+'px;">ဆ</i></a>';
        var icon = '<a href="javascript:void(0);"><i class="layui-icon bt-selectTree-close" style="right:7px;">ဆ</i></a>';
        $(inputElem).after(icon);
        $(inputElem).siblings('a').on('click',function(e){
            bt.stope(e);
            var disabled =  $(this).siblings('input').attr('disabled');
            if (disabled){
                return
            }
            $(this).siblings('input').val('').attr('title','');
            if(_config.clearCallback){
                _config.clearCallback();
            }

            if(_config.checkType=="check" || _config.checkType=="radio"){
                var id = $(this).siblings('.select-tree').find('.select-tree-list').attr('id');
                var checkedElems = $('#'+id).find('ul>li>span.chk');
                var systemSkin = bt.getSysSkin()
                $.each(checkedElems,function(){
                    if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                        if(systemSkin === 'dark-skin'){
                            $(this).siblings('a').css('color','#d3d3d3');
                        }else {
                            $(this).siblings('a').css('color','#000');
                        }

                    }
                })
               that.deleteSelectValue(id);
            }
            //清除数据时，执行回调
            if (_config.clickCallBack && typeof _config.clickCallBack === 'function') {
                _config.clickCallBack('');
            }
        })
    };

    SelectTree.prototype.clearValue = function(selectTreeObj){
        var that = this;
        var _config = selectTreeObj.config;
        // $("#"+selectTreeObj.param.treeInput).val('');
        $("#"+selectTreeObj.param.treeInput).val('').attr("title","");
        $("#"+selectTreeObj.param.treeHiddenInput).val('');
        var zTree = $.fn.zTree.getZTreeObj(selectTreeObj.param.treeList);
        if(zTree){
            if(_config.checkType==="check" || _config.checkType==="radio"){
                var id = $("#"+selectTreeObj.param.treeInput).siblings('.select-tree').find('.select-tree-list').attr('id');
                var checkedElems = $("#"+selectTreeObj.param.treeInput).siblings('.select-tree').find('ul>li>span.chk');
                var systemSkin = bt.getSysSkin()
                $.each(checkedElems,function(){
                    if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                        if(systemSkin === 'dark-skin'){
                            $(this).siblings('a').css('color','#d3d3d3');
                        }else {
                            $(this).siblings('a').css('color','#000');
                        }
                    }
                })
                var nodes = zTree.getCheckedNodes(true);
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    zTree.checkNode(node, false, false);
                }
            }
            zTree.cancelSelectedNode();
        }
    };

    SelectTree.prototype.deleteSelectValue = function(id){
        var ztree = $.fn.zTree.getZTreeObj(id);
        if(this.config.checkType==="check" || this.config.checkType==="radio"){
            var id = $("#"+this.param.treeInput).siblings('.select-tree').find('.select-tree-list').attr('id');
            var checkedElems = $("#"+this.param.treeInput).siblings('.select-tree').find('ul>li>span.chk');
            var systemSkin = bt.getSysSkin()
            $.each(checkedElems,function(){
                if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                    if(systemSkin === 'dark-skin'){
                        $(this).siblings('a').css('color','#d3d3d3');
                    }else {
                        $(this).siblings('a').css('color','#000');
                    }
                }
            })
            var nodes = ztree.getCheckedNodes(true);
            for(var i=0;i<nodes.length;i++){
                var node = nodes[i];
                ztree.checkNode(node, false, false);
            }
        }
        ztree.cancelSelectedNode();
    };

    /**
     * 设置树的选择状态
     * 0.0.5
     * @param ztree
     */
    SelectTree.prototype.setTreeSelectStatus = function(ztree){
        var that = this,_config=that.config;
        if(!_config.selected){
            return;
        }
        var nodes = ztree.transformToArray(ztree.getNodes());
        var valueField = _config.valueField;

        if(typeof (_config.checkType)==="undefined"){
            for(var i=0;i<nodes.length;i++){
                var node = nodes[i];
                //if(_config.selected == node.id){
                if(_config.selected == node[valueField]){
                    // $("#treeInput"+that.randomNumber).val(node.name);
                    $("#treeInput"+that.randomNumber).val(node.name).attr("title",node.name);
                    $("#treeHiddenInput"+that.randomNumber).val(node[valueField]);
                    ztree.selectNode(node);
                    break;
                }
            }
        }else if(_config.checkType==="check" || _config.checkType==="radio"){
            var separator = _config.separator?_config.separator:",";
            var selectedValue = String(_config.selected).split(separator);
            // 取消所有选中
            ztree.checkAllNodes(false);

            for(var i=0;i<nodes.length;i++){
                var node = nodes[i];
                for(var j=0;j<selectedValue.length;j++){
                    if(selectedValue[j] == node[valueField]){
                        ztree.checkNode(node, true, false);
                    }
                }
            }
            nodes = ztree.getCheckedNodes(true);
            var v = "",hiddenId="";
            nodes.sort(function compare(a,b){return a.id - b.id;});
            for (var i=0, l=nodes.length; i<l; i++) {
                v += nodes[i].name + ",";
                hiddenId += nodes[i][valueField] + ",";
            }
            if (v.length > 0 ) v = v.substring(0, v.length-1);

            if (hiddenId.length > 0 ) hiddenId = hiddenId.substring(0, hiddenId.length-1);

            if(_config.checkType=="radio"){
                hiddenId = hiddenId.replace(",","");
            }
            //设置数据选中 liuyj
            for( var i = 0;i < nodes.length;i++){
                ztree.selectNode(nodes[i]);
            }

            //复选时，选中当前节点时，标注红色
            var treeElem = $("#treeInput"+that.randomNumber).siblings('.select-tree');
            if(hiddenId && treeElem[0]){
                var treeConfigKey = treeElem.attr('id');
                var checkedElems = $('#'+treeConfigKey).find('ul>li>span.chk');
                var systemSkin = bt.getSysSkin()
                $.each(checkedElems,function(){
                    if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                        $(this).siblings('a').css('color','red');
                    }else{
                        if(systemSkin === 'dark-skin'){
                            $(this).siblings('a').css('color','#d3d3d3');
                        }else{
                            $(this).siblings('a').css('color','#000');
                        }
                    }
                })
            }

            //if(nodes)
            //ztree.selectNode(nodes);
            // $("#treeInput"+that.randomNumber).val(v);
            $("#treeInput"+that.randomNumber).val(v).attr('title',v);
            $("#treeHiddenInput"+that.randomNumber).val(hiddenId);
        }
    }

    function throwError(msg) {
        throw new Error(msg);
    }

    SelectTree.prototype.onBeforeClick = function(treeId, treeNode, clickFlag) {
        var tree = selectTree.getShowingTree();
        var treeContentId = tree.attr("id");
        var treeConfigKey = treeContentId;
        var currentTreeConfig = layui.selectTree.config[treeConfigKey];
        var valueField = currentTreeConfig.valueField;
        if(currentTreeConfig.checkType === "radio" && treeNode.chkDisabled){
            return false;
        }
        if(currentTreeConfig.beforeClick){
            return currentTreeConfig.beforeClick(treeId, treeNode, clickFlag);
        }

        var id = treeId.replace("treeList","");
        var treeHiddenInput = $("#treeHiddenInput"+id);
        var currentNode = treeHiddenInput.data('current');
        if(currentNode && typeof(currentNode)!=='undefined'){
            currentNode += ',';
            var ids = selectTree.onClickCheckParentNodes(treeNode,"",valueField);
            if(ids.indexOf(currentNode)>=0){
                alert("不可选择子节点");
                return false;
            }
        }
        return true;
    }

    SelectTree.prototype.onClick = function(e, treeId, treeNode) {
        var that = this;
        var id = treeId.replace("treeList","");
        var treeHiddenInput = $("#treeHiddenInput"+id);
        var beforeValue = $("#treeInput"+id).val();
        var nodes;
        //0.0.7新增
        var tree = selectTree.getShowingTree();
        var treeContentId = tree.attr("id");
        var treeConfigKey = treeContentId;
        var currentTreeConfig = layui.selectTree.config[treeConfigKey];
        var valueField = currentTreeConfig.valueField;
        treeContentId = treeContentId.replace("treeContent","");
        var zTree = $.fn.zTree.getZTreeObj("treeList"+treeContentId);

        if( currentTreeConfig.checkType !=="check" ){
            if( treeNode.name != beforeValue ){
                $("#treeInput"+id).val(treeNode.name).attr('title',treeNode.name);
                treeHiddenInput.val(treeNode[valueField]);
                //treeHiddenInput.val(treeNode.id);
            }
            //0.1.0新增
            $("#"+treeConfigKey).removeClass("select-tree-selected").removeClass("select-tree-selected-up");
        }

        //0.1.7 新增
        if( currentTreeConfig.checkType && currentTreeConfig.checkType !=="check"){
            zTree.checkAllNodes(false);
            zTree.checkNode(treeNode, true, false);
        }
        var nodeId = treeNode[valueField];
        // 0.1.14 新增加 复选框时点击文字可以多次选择或取消选中内容，不关闭下拉树 liuyj 2018-07-03
        if( currentTreeConfig.checkType =="check" || currentTreeConfig.checkType =="radio" ){
            var isCheck = beforeValue.indexOf(treeNode.name) > -1 ? false:true;
            var separator = currentTreeConfig.separator?currentTreeConfig.separator:",";
            zTree.checkNode(treeNode, isCheck, false);
            nodes = zTree.getCheckedNodes(true),
                v = "",hiddenId="";
            nodes.sort(function compare(a,b){return a.id- b.id;});
            for (var i=0, l=nodes.length; i<l; i++) {
                v += nodes[i].name + separator;
                hiddenId += nodes[i][valueField] + separator;
            }
            if (v.length > 0 ) v = v.substring(0, v.length-1);
            if (hiddenId.length > 0 ) hiddenId = hiddenId.substring(0, hiddenId.length-1);
            $("#treeInput"+treeContentId).val(v).attr("title",v);
            $("#treeHiddenInput"+treeContentId).val(hiddenId);
            if( !isCheck ){
                nodeId = '';
            }
            nodeId = hiddenId;

            //复选时，选中当前节点时，标注红色
            var checkedElems = $('#'+treeConfigKey).find('ul>li>span.chk');
            var systemSkin = bt.getSysSkin()
            $.each(checkedElems,function(){
                if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                    $(this).siblings('a').css('color','red');
                }else{
                    if(systemSkin === 'dark-skin'){
                        $(this).siblings('a').css('color','#d3d3d3');
                    }else{
                        $(this).siblings('a').css('color','#000');
                    }
                }
            })
        }else {
            nodes = [treeNode]
        }

        //获取当前树的配置，回调点击方法
        if(currentTreeConfig.clickCallBack ){
            currentTreeConfig.clickCallBack( nodeId );
        }
        //0.0.9新增
        if(currentTreeConfig.clickCallBackData){
            // currentTreeConfig.clickCallBackData(treeNode);
            if(currentTreeConfig.setting.check && currentTreeConfig.setting.check.chkStyle !== "checkbox"){
                currentTreeConfig.clickCallBackData(nodes[0]);
            }else{
                currentTreeConfig.clickCallBackData(nodes);
            }
        }

        //0.1.18 新增　选中时，清除其它选中样式
        tree.find('.search_result').removeClass('search_result');
        // 显示的数据显示出来
        if (selectTree.hiddenTreeNodes.hiddenNodes){
            zTree.showNodes(selectTree.hiddenTreeNodes.hiddenNodes);
            selectTree.hiddenTreeNodes.treeList = '';//置空
            selectTree.hiddenTreeNodes.hiddenNodes = [];
        }
    }

    SelectTree.prototype.onClickCheckParentNodes = function(treeNode,ids,valueField){
        ids += treeNode[valueField] + ',';
        var parentNode = treeNode.getParentNode();
        if(parentNode){
            return selectTree.onClickCheckParentNodes(parentNode,ids,valueField);
        }
        return ids;
    }

    SelectTree.prototype.onCheck = function(e, treeId, treeNode){
        var that = this;
        var tree = selectTree.getShowingTree();
        var treeContentId = tree.attr("id");
        var treeConfigKey = treeContentId;
        //0.0.7新增
        //获取当前树的配置
        var currentTreeConfig = layui.selectTree.config[treeConfigKey];
        var separator = currentTreeConfig.separator?currentTreeConfig.separator:",";

        treeContentId = treeContentId.replace("treeContent","");
        var zTree = $.fn.zTree.getZTreeObj("treeList"+treeContentId),
            nodes = zTree.getCheckedNodes(true),
            v = "",hiddenId="";
        nodes.sort(function compare(a,b){return a.id-b.id;});
        for (var i=0, l=nodes.length; i<l; i++) {
            v += nodes[i].name + separator;
            //hiddenId += nodes[i].id + separator;
            hiddenId += nodes[i][currentTreeConfig.valueField] + separator;
        }
        if (v.length > 0 ) v = v.substring(0, v.length-1);
        if (hiddenId.length > 0 ) hiddenId = hiddenId.substring(0, hiddenId.length-1);
        // $("#treeInput"+treeContentId).val(v);
        $("#treeInput"+treeContentId).val(v).attr("title",v);
        $("#treeHiddenInput"+treeContentId).val(hiddenId);

        if(currentTreeConfig.setting.check.chkStyle === "radio"){
            $("#"+treeConfigKey).removeClass("select-tree-selected").removeClass("select-tree-selected-up");
            zTree.cancelSelectedNode();//0.1.0如果是单选，选择完成后隐藏
            zTree.selectNode(treeNode,true);
        }

        //0.0.7新增
        //回调点击方法
        if(currentTreeConfig.clickCallBack){
            currentTreeConfig.clickCallBack(hiddenId);
        }
        //复选时，选中当前节点时，标注红色
        if( currentTreeConfig.checkType =="check" ){
            var checkedElems = $('#'+treeConfigKey).find('ul>li>span.chk');
            var systemSkin = bt.getSysSkin()
            $.each(checkedElems,function(){
                if($(this).hasClass('checkbox_true_part')|| $(this).hasClass('checkbox_true_full') || $(this).hasClass('checkbox_true_full_focus')){
                    $(this).siblings('a').css('color','red');
                }else{
                    if(systemSkin === 'dark-skin'){
                        $(this).siblings('a').css('color','#d3d3d3');
                    }else{
                        $(this).siblings('a').css('color','#000');
                    }
                }
            })
        }
        //0.0.9新增
        if(currentTreeConfig.clickCallBackData){
            if(currentTreeConfig.setting.check.chkStyle === "radio"){
                currentTreeConfig.clickCallBackData(nodes[0]);
            }else{
                currentTreeConfig.clickCallBackData(nodes);
            }
        }

        //0.1.18 新增　选中时，清除其它选中样式
        tree.find('.search_result').removeClass('search_result');
        // 将检索后不显示的数据显示出来
        if ( selectTree.hiddenTreeNodes.hiddenNodes ) {
            zTree.showNodes(selectTree.hiddenTreeNodes.hiddenNodes);
            selectTree.hiddenTreeNodes.treeList = '';//置空
            selectTree.hiddenTreeNodes.hiddenNodes = [];
        }
    }

    SelectTree.prototype.showSelectTree = function () {
        var that = this;
        var treeContent = $("#"+that.param.treeContent);
        var treeInput = $("#"+that.param.treeInput);
        //定位
        var top = treeInput.offset().top + treeInput.outerHeight() + 5 - $(window).scrollTop();
        var treeContentHeight = treeContent.outerHeight();
        if(top + treeContentHeight > $(window).height() && top >= treeContentHeight){
            treeContent.addClass("select-tree-selected-up");
        }else{
            //当页面上有固定位置按钮时，定位向下时，需要增加固定按钮高度  liuyj 2018-08-06
            if($('.layui-form-submit').length){
                if(top + treeContentHeight + 50 > $(window).height() && top >= treeContentHeight){
                    treeContent.addClass("select-tree-selected-up");
                }else{
                    treeContent.addClass("select-tree-selected");
                }
            }else{
                treeContent.addClass("select-tree-selected");
            }
        }
    }

    SelectTree.prototype.hideSelectTreeEvent = function(event) {
        var cid = $(event.target).attr('id');
        if((typeof (cid) != 'undefined') && (cid.indexOf("treeContent") !=-1 || cid.indexOf("treeList") !=-1 || cid.indexOf("treeInput") !=-1)){
            return;
        }
        selectTree.hideSelectTree('hideSelectTree');
    }

    SelectTree.prototype.hideSelectTree = function(isClose) {
        var that = this;
        var tree = this.getShowingTree();
        // 将检索后不显示的数据显示出来
        var treeList = this.hiddenTreeNodes.treeList;
        if( treeList){
            var treeObj = $.fn.zTree.getZTreeObj(treeList);
            if( treeObj ){
                $( '#'+treeList ).find('.layui-hide').removeClass('layui-hide');
                treeObj.showNodes(this.hiddenTreeNodes.hiddenNodes);
                this.hiddenTreeNodes.treeList = '';//置空
                this.hiddenTreeNodes.hiddenNodes = [];
            }
        }
        $('.tree .search_result').removeClass('search_result');

        if ( isClose && tree && treeList){
            var nodes = treeObj.getSelectedNodes();//获取选中数据
            if(!nodes.length){ // 无选中数据时，直接清空
                $('#'+that.param.treeInput).val('').attr("title",'');
                $('#' + that.param.treeHiddenInput ).val('');

            }else{
                $('#'+that.param.treeInput).val($('#'+that.param.treeInput).attr("title"));
            }
        }

        if(tree){
            tree.removeClass("select-tree-selected");
            tree.removeClass("select-tree-selected-up");
        }

    }

    SelectTree.prototype.getShowingTree = function(){
        var treeObj = null;
        //查找页面上是否有selectTrre展示，若有记录当前的selectTree
        $(".select-tree").each(function(){
            var tree =$(this);
            if(tree.hasClass("select-tree-selected") || tree.hasClass("select-tree-selected-up")){
                treeObj = tree;
                return;
            }
        });
        return treeObj;
    }

    //0.0.8新增
    SelectTree.prototype.setData = function(data){
        var that = this;
        that.config.param = data;
        that.initTree();
    }

    SelectTree.prototype.setRequired = function(isRequired){
        var that = this;
        that.config.required = isRequired;
        if(isRequired){
            $('#'+that.param.treeInput).attr("lay-verify",'required');
        }else{
            $('#'+that.param.treeInput).removeAttr('lay-verify');
        }
        that.initTree();
    }

    SelectTree.prototype.setUrl = function(url){
        var that = this;
        that.config.url = url;
        // 新增加设置url时，请空input的值 liuyj 2019-05-17
        $('#'+that.param.treeInput).val('').attr("title",'');
        $('#' + that.param.treeHiddenInput ).val('');

        that.initTree();
    }

    //0.1.1 新增，获取和设置值
    SelectTree.prototype.val = function(value){
        var that = this;

        if(value){
            var treeObj = $.fn.zTree.getZTreeObj(that.param.treeList);
            that.config.selected = value;
            that.setTreeSelectStatus(treeObj);
        }else if(typeof (value) === 'undefined'){
            return $('#'+that.param.treeHiddenInput).val();
        }else{
            that.clearValue(that);
        }
    }

    //0.1.1 新增，获取和设置值
    SelectTree.prototype.data = function(value){
        var that = this;
        var _config = that.config;
        if(value){
            var treeObj = $.fn.zTree.getZTreeObj(that.param.treeList);
            var nodes;

            if(typeof (that.config.checkType)=="undefined"){
                nodes = treeObj.getSelectedNodes();
            }else if(that.config.checkType=="check" || _config.checkType=="radio"){

            }
            that.config.selected = value;
            that.setTreeSelectStatus(treeObj);
        }else{
            var treeObj = $.fn.zTree.getZTreeObj(that.param.treeList);
            var nodes;
            if(typeof (that.config.checkType)=="undefined"){
                nodes = treeObj.getSelectedNodes();
            }else if(that.config.checkType=="check" || _config.checkType=="radio"){
                nodes = treeObj.getCheckedNodes(true);
            }
            return nodes;
        }
    }

    var selectTree;
    var obj = {
        init: function(option){
            selectTree = new SelectTree();
            selectTree.init(option);
            return this;
        },
        initGetCurrent: function(option){
            selectTree = new SelectTree();
            selectTree.init(option,true);
            return selectTree;
        },
        //0.0.8新增
        setData: function(data){
            selectTree.setData(data);
        },
        setUrl: function(url){
            selectTree.setUrl(url);
            return this;
        }
    };
    //输出test接口
    exports('selectTree', obj);
});

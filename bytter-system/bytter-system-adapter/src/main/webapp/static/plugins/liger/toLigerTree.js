/**
 * 普通树表格转换固定列树表格
 *
 * 刘勇军
 * 0.0.1 自定义宽度通过data-width，默认均分
 * 0.0.2 宽度采用原因表格，新增align参数
 * 0.0.3 新增树表格操作列隐藏下拉框
 * 0.0.4 表格隐藏设置兼容列不冻结情况
 * */
layui.define(["jquery","ligerTreeTable"],function(exports){
    var jQuery=layui.jquery;

    var LigerTreeTable = function(){
        this.config = {
            elem:'',
            newElem:'',
            rownumbers:false,//是否有序号列 false为没有,true代表elem容器内有序列号
            isHasRownumberColumn:false,//页面上是否已经配置了序号列
            isDoubleHeader:false,//是否有双表头
            columnName:'name',//树列名称
            allowHideColumn:false,//隐藏列功能关闭
            fixedLeftColumns:2,//默认固定左边两列，不包括序号列
            resize:true,//列托宽功能,默认开启
            height:'auto',
            width:'100%'
        };
        this.currentObject = null;
    }
    /**
     * 重置参数
     *
     * */
    LigerTreeTable.prototype.set = function(ops){
        $.extend(true, this.config, ops);
        //解决底部固定定位按钮档住数据的问题，原因：冻结表格与其它元素并列，设置数容器的高度为0，使得body padding-bottom无效
        if ($(this.config.elem).siblings()[0]){
            $(this.config.elem).wrap('<div style="height:0;"></div>');
        }else{
            $(this.config.elem).parent().css('height','0px').show();//旧表格出现宽度值
        }

    }

    /**
     * 树型表格渲染
     * */
    LigerTreeTable.prototype.render = function(isMultiTree){

        var that = this;
        var config = this.config;
        config.isHasRownumberColumn = config.rownumbers;//获取页面上是否已经配置了序列号
        if (config.isHasRownumberColumn) {
            $(config.elem).addClass('ligerui-table');
        }
        /**
         * 由于不同的浏览器高度不一致 及有一些页面是通过后台获取的，造成无法确定添加冻结树表格后，是否有纵向滚动条，有滚动条，它的宽度将会减去17px；
         * 设计时默认有滚动条
         */
        // config.width = $(config.elem).width() -17;

        $(config.newElem).height(900); //添加一个高度，让它产生纵向滚动条

        var refObj =  getAverageTdWidth(config);//获取宽度数组 居中方法
        var tdsWidthArr = refObj.tdsWidth;//获取宽度数组
        var align = refObj.align;//居中方式

        var rowData = treeBody(config);//获取行数据
        var columnsData = treeColumn(config,tdsWidthArr,align);//获取列数据
        var height = config.height;
        /**
         * 设置px高度自适性
         * 1、当传入的px数小于或等于表格高度时，转化为对应的%传入；
         * 2、当传入的px数大于表格高度时，转化为‘auto‘传入
         * */
        var length = $(config.elem).find('tr').length;
        var maxHeight = (length+1)*28;//最大高度加上表头
        if( typeof height == "string" && height.indexOf('px')!= -1 ){
            if(  parseInt(maxHeight) > parseInt(height) ){
                config.height = Math.floor(( parseInt(height)*100 / $(window).height()))+ '%';
            }else{
                config.height = 'auto';
            }
        }else{
            if(  parseInt(maxHeight) < parseInt(height)*$(window).height()/100){ //表格本身高度小于传参高度时，采用表格本身高度
                //config.height = Math.ceil(( parseInt(maxHeight)*100 / $(window).height()))+ '%';
                config.height = 'auto';
            }
        }

        var obj = {
            elem:config.newElem,
            rownumbers:config.rownumbers,
            columns:columnsData,
            data:{Rows:rowData},
            tree: { columnName: config.columnName},
            allowHideColumn:config.allowHideColumn ? true:false,
            height:config.height,
            width:config.width,
            resize:config.resize,
            frozenRownumbers:config.fixedLeftColumns?true:false
        }
        if(config.onRendered && typeof config.onRendered === 'function'){
            obj.onRendered = config.onRendered
        }

        if(config.beforeInit && typeof config.beforeInit === 'function'){
            obj = config.beforeInit(obj);
        }

        if(isMultiTree){ // 初始化多颗树
            bt.ligerTreeTable.initMulTreeTable(obj);
        }else{
            bt.ligerTreeTable.init(obj);
        }


        $(config.newElem).height('auto');//取消添加的高度

        //如果有打印控件时，把旧的表格html存贮起来
        if($('.bt-biz-printType')[0]){
            window.printType = {}
            window.printType['printHtml'] = $(config.elem).html();
        }

        $(config.elem).remove();
        $(config.newElem).find(".l-grid-popup input").attr('lay-ignore','');
    }

    /**
     * 树型表格渲染
     * */
    LigerTreeTable.prototype.render2 = function(ops){

        var that = this;
        var config = this.config;
        config.isHasRownumberColumn = config.rownumbers;//获取页面上是否已经配置了序列号
        if (config.isHasRownumberColumn) {
            $(config.elem).addClass('ligerui-table');
        }
        /**
         * 由于不同的浏览器高度不一致 及有一些页面是通过后台获取的，造成无法确定添加冻结树表格后，是否有纵向滚动条，有滚动条，它的宽度将会减去17px；
         * 设计时默认有滚动条
         */
        // config.width = $(config.elem).width() -17;

        $(config.newElem).height(900); //添加一个高度，让它产生纵向滚动条

        var refObj =  getAverageTdWidth(config);//获取宽度数组 居中方法
        var tdsWidthArr = refObj.tdsWidth;//获取宽度数组
        var align = refObj.align;//居中方式

        var rowData = treeBody2(config);//获取行数据
        var columnsData = treeColumn2(config,tdsWidthArr,align);//获取列数据
        var height = config.height;
        /**
         * 设置px高度自适性
         * 1、当传入的px数小于或等于表格高度时，转化为对应的%传入；
         * 2、当传入的px数大于表格高度时，转化为‘auto‘传入
         * */
        var length = $(config.elem).find('tr').length;
        var maxHeight = (length+1)*28;//最大高度加上表头
        if( typeof height == "string" && height.indexOf('px')!= -1 ){
            if(  parseInt(maxHeight) > parseInt(height) ){
                config.height = Math.floor(( parseInt(height)*100 / $(window).height()))+ '%';
            }else{
                config.height = 'auto';
            }
        }else{
            // if(  parseInt(maxHeight) < parseInt(height)*$(window).height()/100){ //表格本身高度小于传参高度时，采用表格本身高度
            if(  parseInt(maxHeight) < 450){ //表格本身高度小于450时，表格垂直方法不出现滚动条
                //config.height = Math.ceil(( parseInt(maxHeight)*100 / $(window).height()))+ '%';
                config.height = 'auto';
            }else {
                //当页面超过450时，设置最大高度显示
                config.height = "462px";
            }
        }
        bt.ligerTreeTable.init({
            elem:config.newElem,
            rownumbers:config.rownumbers,
            columns:columnsData,
            data:{Rows:rowData},
            tree: {
                columnName: config.columnName
            },
            // allowHideColumn:config.allowHideColumn ? true:false,
            allowHideColumn:false,
            height:config.height,
            width:config.width,
            resize:config.resize,
            isExpand:false,
            onAfterShowData:function(data){
                that.updateVisibleColumns();
                if (config.onAfterShowData && typeof config.onAfterShowData === "function"){
                    config.onAfterShowData.call(this,data);
                }
            },
            frozenRownumbers:config.fixedLeftColumns?true:false
        });

        //创建操作工具
        this.createToolsContanier(columnsData);
        $(config.newElem).height('auto');//取消添加的高度

        //如果有打印控件时，把旧的表格html存贮起来
        if($('.bt-biz-printType')[0]){
            window.printType = {}
            window.printType['printHtml'] = $(config.elem).html();
        }

        $(config.elem).remove();
        $(config.newElem).find(".l-grid-popup input").attr('lay-ignore','');
    }
    /**
     * 添加子数据方法
     * parentId:父级ＩＤ
     * data:需要添加的数据
     * alldata:总数据
     * */
    var createChildData = function(parentId,data,allData){
        var isSearch = false;
        for(var i = 0;i< allData.length;i++){
            if(allData[i]['id'] === parentId){
                if(allData[i]['children'] && allData[i]['children'].length){
                    allData[i]['children'].push(data)
                }else{
                    allData[i]['children'] = [data];
                }
                isSearch = true;
                break;
            }else{
                if(allData[i]['children']){
                    isSearch = createChildData(parentId,data,allData[i]['children']);
                    if(isSearch){
                        break
                    }
                }
            }
        }
        if(isSearch){
            return isSearch;
        }
    }
    /**
     * 获取表头数据
     * */
    var treeHeader = function(setting){
        var headerText = [];
        var rowspanData = [];
        var trs = $(setting.elem).find('thead tr')
        $.each(trs,function(index){
            headerText[index] = [];
            var ths = $(this).find('th');
            $.each(ths,function(i){

                headerText[index].push($(this).html());
            })
        })
        return headerText;
    }
    /**
     *  获取表体数据
     * */
    var treeBody = function(setting){
        var trs = $(setting.elem).find('tbody tr');
        var rows =[];
        var trObj = {};
        var isTopData = false;
        $.each(trs,function(index,item){
            var id = $(this).data('tt-id');
            var parentId = $(this).data('tt-parent-id');
            if(parentId ===''&& JSON.stringify(trObj) !=='{}'){
                rows.push(trObj);
                trObj = {};
            }
            var currentObj = {};
            if(setting.isHasRownumberColumn){ //有序号列
                currentObj.name = $(this).find('td:eq(1)').html();
            }else{
                currentObj.name = $(this).find('td:eq(0)').html();
            }
            currentObj.id = id;
            if( parentId ===''){
                trObj = currentObj
            }else{
                if( trObj['id'] == parentId){
                    if(trObj['children'] && trObj['children'].length){
                        trObj['children'].push(currentObj)
                    }else{
                        trObj['children'] = [currentObj];
                    }
                }else{
                    if(trObj['children']){
                        createChildData(parentId,currentObj,trObj['children'])
                    }else{
                        rows.push(trObj);
                        trObj = {};
                    }
                }
            }
            //当数据为最后一条时，将数据直接添加到数组中
            if(index === trs.length-1){
                rows.push(trObj);
            }
        })
        return rows
    }

    /**
     *  获取表体数据
     * */
    var treeBody2 = function(setting){
        var trs = $(setting.elem).find('tbody tr');
        var rows =[];
        var trObj = {};
        var isTopData = false;
        $.each(trs,function(index,item){
            var id = $(this).data('tt-id');
            var parentId = $(this).data('tt-parent-id');
            if(parentId ===''&& JSON.stringify(trObj) !=='{}'){
                rows.push(trObj);
                trObj = {};
            }
            var currentObj = {
                id:id
            };
            var tds = $(this).find('td')
            bt.each(tds,function () {
                var name = $(this).data('name');
                if(name){
                    currentObj[name] = $(this).html();
                }
            })
            if( parentId ===''){
                trObj = currentObj
            }else{
                if( trObj['id'] == parentId){
                    if(trObj['children'] && trObj['children'].length){
                        trObj['children'].push(currentObj)
                    }else{
                        trObj['children'] = [currentObj];
                    }
                }else{
                    if(trObj['children']){
                        createChildData(parentId,currentObj,trObj['children'])
                    }else{
                        rows.push(trObj);
                        trObj = {};
                    }
                }
            }
            //当数据为最后一条时，将数据直接添加到数组中
            if(index === trs.length-1){
                rows.push(trObj);
            }
        })
        return rows
    }

    var treeColumn = function(setting,tdsWidthArr,align){
        var columns = [];
        var h = treeHeader(setting)[0];
        var table = $(setting.elem);
        var columnsLength = table.find('tbody tr:eq(0)').find('td').length;
        var $tr = table.find('tbody tr:eq(0)');
        var startColumnIndex = 0;
        var fixedLeftColumns = setting.fixedLeftColumns;

        if(setting.isHasRownumberColumn){
            startColumnIndex = 1;
            fixedLeftColumns = fixedLeftColumns + 1;
        }
        if(!setting.isDoubleHeader){
            for(var i = startColumnIndex;i < columnsLength;i++){
                var obj = {};
                obj.display = h[i];
                obj.name ='id';
                if(tdsWidthArr[i]){
                    obj.width = tdsWidthArr[i];
                }
                obj.align = align[i]
                if(i < fixedLeftColumns && fixedLeftColumns){
                    obj.frozen = true;
                }
                if(i === startColumnIndex){
                    obj.name ='name';
                }else{
                    if(i === 1){
                        obj.name ='id';
                    }
                    obj.render = function(record, rowindex, value, column){
                        //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                        return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').html();
                    }
                }
                columns.push(obj);
            }
        }else{
            var secondColumnIndex = 0;
            var firstThs = table.find('thead tr:eq(0)').find('th');
            var columnIndex = startColumnIndex;//

            for(var i = startColumnIndex;i < firstThs.length;i++){
                var obj = {};
                var $this = $(firstThs[i]);
                obj.display = $this.html();
                var columnsSpanCount = $this.attr('colspan');
                var rowSpanCount = $this.attr('rowspan');
                obj.name ='id';
                if(tdsWidthArr[columnIndex]){
                    obj.width = tdsWidthArr[columnIndex];
                }
                obj.align = align[i];
                columnIndex += 1;

                if( i < fixedLeftColumns && fixedLeftColumns){
                    obj.frozen = true;
                }
                if(i === 0 && !setting.isHasRownumberColumn){
                    obj.name ='name';
                }
                if( i === 1 && setting.isHasRownumberColumn){
                    obj.name ='name';
                }else{
                    if( rowSpanCount ){

                        obj.render = function(record, rowindex, value, column){
                            //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                            //columnindex = column.columnindex;
                            return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').clone().html();
                        }
                    }else{
                        if(obj.columns === undefined){
                            obj.columns =[];
                        }
                        columnsSpanCount = columnsSpanCount ?　columnsSpanCount :1;
                        var columnsSpanWidth= 0;
                        var isPercentWidth = false;
                        for(var k = 0; k < columnsSpanCount;k++){
                            var childObj = {};
                            childObj.name ='id';
                            childObj.display = table.find('thead tr:eq(1)').find('th:eq('+secondColumnIndex+')').html();
                            if(tdsWidthArr[columnIndex -1]){
                                childObj.width = tdsWidthArr[columnIndex -1];
                                columnsSpanWidth += childObj.width;

                            }

                            childObj.render = function(record, rowindex, value, column){
                                //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                                //columnindex = column.columnindex;
                                return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').html();

                            }
                            obj.columns.push(childObj);
                            secondColumnIndex ++;
                            if( k < columnsSpanCount - 1){
                                columnIndex += 1;
                            }
                        }
                        if(columnsSpanWidth){
                            obj.width = columnsSpanWidth;
                            if(isPercentWidth){
                                obj.width = obj.width + '%';
                            }
                        }
                    }
                }
                columns.push(obj);
            }
        }
        return columns
    }

    var treeColumn2 = function(setting,tdsWidthArr,align){
        var columns = [];
        var h = treeHeader(setting)[0];
        var table = $(setting.elem);
        var firstTrTds = table.find('tbody tr:eq(0)').find('td');
        var columnsLength = firstTrTds.length;
        var $tr = table.find('tbody tr:eq(0)');
        var startColumnIndex = 0;
        var fixedLeftColumns = setting.fixedLeftColumns;

        if(setting.isHasRownumberColumn){
            startColumnIndex = 1;
            fixedLeftColumns = fixedLeftColumns + 1;
        }
        if(!setting.isDoubleHeader){
            for(var i = startColumnIndex;i < columnsLength;i++){
                var obj = {};
                obj.display = h[i];
                obj.name = $(firstTrTds[i]).data('name');
                if(tdsWidthArr[i]){
                    obj.width = tdsWidthArr[i];
                }
                obj.align = align[i]
                if(i < fixedLeftColumns && fixedLeftColumns){
                    obj.frozen = true;
                }
                // if(i === startColumnIndex){
                //     obj.name ='name';
                // }else{
                //     if(i === 1){
                //         obj.name ='id';
                //     }
                //     obj.render = function(record, rowindex, value, column){
                //         //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                //         return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').html();
                //     }
                // }
                columns.push(obj);
            }
        }else{
            var secondColumnIndex = 0;
            var firstThs = table.find('thead tr:eq(0)').find('th');
            var columnIndex = startColumnIndex;//
            var tdIndex = startColumnIndex; //td 索引，用于获取name用

            for(var i = startColumnIndex;i < firstThs.length;i++){
                var obj = {};
                var $this = $(firstThs[i]);
                obj.display = $this.html();
                var columnsSpanCount = $this.attr('colspan');
                var rowSpanCount = $this.attr('rowspan');
                if(!columnsSpanCount || (columnsSpanCount & columnsSpanCount > 1)){
                    obj.name = $(firstTrTds[tdIndex]).data('name');
                }
                if(tdsWidthArr[tdIndex]){
                    obj.width = tdsWidthArr[tdIndex];
                }
                obj.align = align[tdIndex];
                columnIndex += 1;

                if( i < fixedLeftColumns && fixedLeftColumns){
                    obj.frozen = true;
                }
                if(i === 0 && !setting.isHasRownumberColumn){
                    obj.name =$ (firstTrTds[i]).data('name');
                }
                if( i === 1 && setting.isHasRownumberColumn){
                    obj.name = $(firstTrTds[i]).data('name')

                }else{
                    if( rowSpanCount ){
                        // obj.render = function(record, rowindex, value, column){
                        //     //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                        //     //columnindex = column.columnindex;
                        //     return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').clone().html();
                        // }

                    }else{
                        if(obj.columns === undefined){
                            obj.columns =[];
                        }
                        columnsSpanCount = columnsSpanCount ?　columnsSpanCount :1;
                        var columnsSpanWidth= 0;
                        var isPercentWidth = false;
                        for(var k = 0; k < columnsSpanCount;k++){
                            var childObj = {};
                            // tdIndex =  tdIndex + k;
                            if(k){
                                tdIndex ++;
                            }
                            childObj.name = $(firstTrTds[tdIndex]).data('name');
                            childObj.display = table.find('thead tr:eq(1)').find('th:eq('+secondColumnIndex+')').html();
                            if(tdsWidthArr[columnIndex -1]){
                                childObj.width = tdsWidthArr[columnIndex -1];
                                columnsSpanWidth += childObj.width;

                            }
                            // childObj.render = function(record, rowindex, value, column){
                            //     //var columnindex = setting.isHasRownumberColumn ? column.columnindex:(column.columnindex -1);
                            //     //columnindex = column.columnindex;
                            //     return table.find('tbody tr:eq('+rowindex+')').find('td:eq('+column.columnindex+')').html();
                            //
                            // }

                            obj.columns.push(childObj);
                            secondColumnIndex ++;
                            if( k < columnsSpanCount - 1){
                                columnIndex += 1;
                            }
                        }
                        if(columnsSpanWidth){
                            obj.width = columnsSpanWidth;
                            if(isPercentWidth){
                                obj.width = obj.width + '%';
                            }
                        }
                    }
                }
                tdIndex ++;
                columns.push(obj);
            }
        }
        return columns
    }
    /**
     * 列宽均分方法
     * 自定义宽度采用data-width设置
     * 树列宽度默认为140
     * */
    var getAverageTdWidth = function(setting){
        //获取宽度传入的值,不包括序列号
        var tdsWidth = [];
        var tds = $(setting.elem).find('tbody tr:eq(0)').find('td');
        var fixedLeftColumns = setting.fixedLeftColumns;
        var rownumbers = setting.rownumbers;

        var isHasWidthSum = 0;//获取有传入宽度值的总和;
        var isHasWidthCount = 0;//获取有传入宽度值的列数
        // 获取新的树表格容器的宽度；
        var elemWidth = $(setting.newElem).width();

        var isRownumberWidth = 0; //序号列宽度

        var averageScrollWidthByTd = 0;//按照行数均滚动条宽度
        var gap = 0;//均分后的差异

        if (tds.length){ //默认都有滚动条
            var length = tds.length
            averageScrollWidthByTd = Math.floor(17/(length - 1));
            gap = 17 - averageScrollWidthByTd * (length -1);
        }

        var alignArr = [];

        bt.each(tds,function(index,item){
            var width = $(this).outerWidth()-2;
            // if( rownumbers && index == 0 ){ //说明第一格为序号列时，宽度给到第二列
            //     isRownumberWidth = width - 25;
            // }
            // if(rownumbers && isRownumberWidth && index == 1){ //有序号列时
            //     width = isRownumberWidth + width;//将序号的宽度加到第二列中
            //
            // }

            if( rownumbers && index != 0 && width < 140){
                width = 140;
            }

            if(index){ //从列索引1开始，均减纵向滚动条的宽度
                width = width - averageScrollWidthByTd;
            }
            if ( index === (length - 1) ){
                width = width - gap;//滚动条均分后的差异
                if (length > 5){ //列数越多，会出现误差
                    width += Math.floor(length / 2);
                }
            }

            var align = $(this).attr('align');
            align = align?align:'left';//如果没有传入，直接为0
            alignArr.push(align)

            tdsWidth.push(width)
        })

        return {tdsWidth:tdsWidth,align:alignArr,isHasRownumberColumn:isRownumberWidth?true:false}
    }

    //创建工具容器
    LigerTreeTable.prototype.createToolsContanier = function(columnsData){
        if (this.config.allowHideColumn){
            $(this.config.newElem).wrap('<div class="tree-table-container"></div>');
            var num = this.randomNum();
            var html = '<div class="tree-table-operate">';
            html += '<button type="button" id="handleBtn'+num+'">列隐藏</button>';
            html += '<div id="colsBody'+num+'" class="cols-body"></div>';
            html += '</div>';
            $(this.config.newElem).parent('.tree-table-container').prepend(html);
            this.createSelectElemForHandleVisibleColumn(columnsData,num);
            this.onHandleEvent(num);
        }
    }
    //创建操作列显示隐藏的下拉框
    LigerTreeTable.prototype.createSelectElemForHandleVisibleColumn = function(columnsData,num){
        /*
       * 需要隐藏列的起始列索引
       * 当存在右冻结列时，needHiddenColStartIndex 为0
       * 当不存在右冻结列时，needHiddenColStartIndex 为减去序号列不隐藏
       * */
        var needHiddenColStartIndex = 0;
        if (!this.config.fixedLeftColumns){
            if (this.config.rownumbers || this.config.isHasRownumberColumn){
                needHiddenColStartIndex = 1;
            }
        }
        if (columnsData && columnsData.length){
            var columns = [];
            var colIndex = needHiddenColStartIndex;
            var bodyColIndex = needHiddenColStartIndex;
            bt.each(columnsData,function (i, column) {
                if (!column.frozen){
                    var obj = {
                        title:column.display,
                        name:column.name,
                        colIndex:colIndex
                    }
                    colIndex ++;
                    if (column.columns && column.columns.length){
                        obj.columns = [];
                        var hasTitle = true;
                        bt.each(column.columns,function (index,item) {
                            var obj1 = {
                                title:item.display,
                                name:item.name,
                                colIndex:bodyColIndex
                            }
                            if (item.display === undefined){
                                hasTitle = false;
                            }
                            obj.columns.push(obj1)
                            bodyColIndex ++
                        })
                        if (!hasTitle){ //如果子元素出现无title，则父也要改成无title
                            obj.title = undefined;
                        }
                    }else {
                        bodyColIndex ++
                    }
                    columns.push(obj)
                }
            })
          //数据中，title为undefined时，子父都禁隐藏列
            var html = '<ul>';
            var bodyIndex = needHiddenColStartIndex;
            bt.each(columns,function (index,item) {
                var hasChildCol = false;
                var colspan = 0;
                var hasTitle = item.title !== undefined;
                if(item.columns && item.columns.length){
                    hasChildCol = true;
                    colspan = item.columns.length;
                }
                if (hasTitle){
                    html += '<li data-col="'+item.colIndex+'" data-columnname="'+item.name+'"';
                }
                if (hasChildCol && hasTitle){
                    html += 'data-children="'+ colspan +'"  class="cols-parent">'
                }else {
                    if (hasTitle){
                        html += 'data-bodyIndex="'+bodyIndex+'" ';
                        html += '>';
                        html += '<input type="checkbox"  class="col-checkbox" data-col="'+item.colIndex+'" lay-ignore="" style="display: inline-block;margin-right:10px;">';
                    }
                    bodyIndex ++;
                }
                if (hasTitle){
                    html += item.title;
                    html += '</li>';
                }
                if(hasChildCol){
                    html += '<ul>';
                    bt.each(item.columns,function (i,col) {
                        if (col.title !== undefined){
                            html += '<li data-col="'+col.colIndex+'" data-parent="'+item.colIndex+'" data-columnname="'+col.name+'">';
                            html += '<input type="checkbox" class="col-checkbox" data-col="'+col.colIndex+'" lay-ignore="" style="display: inline-block;margin-right: 10px;">';
                            html += col.title;
                            html += '</li>';
                        }
                        bodyIndex ++;
                    })
                    html += '</ul>'
                }
            })
            html += '</ul>'
            $("#colsBody"+num).html(html)
        }
    }

    //注册操作事件
    LigerTreeTable.prototype.onHandleEvent = function(num){
        $(document).on('click',function () {
            setTimeout(function(){  $("#colsBody"+num).hide(); }, 150);
        })
        $("#handleBtn"+num).on('click',function () {
            $("#colsBody"+num).show();
            return false;
        })
        $("#colsBody"+num).on('click','li',function (e) {

            var colIndex = $(this).data('col');
            var columnname = $(this).data('columnname');
            var parentColIndex = $(this).data('parent');
            var hasChildCol =  $(this).data('children'); //是否有子表头
            if (hasChildCol){　//如果有，父子进行联动
                return false
            }
            var visibleChildLis = 0;
            $(this).toggleClass('active');
            if (e.target.type !== 'checkbox'){
                $(this).find('input').prop('checked',$(this).hasClass('active'));
            }
            if (parentColIndex !== undefined){
                visibleChildLis = $(this).parent('ul').children('li:not(.active)').length;
            }
            var headerTrs = $('#maingrid1').find('.l-grid-header2').find('tr');
            var bodyTrs =  $('#maingrid1').find('.l-grid-body2').find('tr');
            bt.each(headerTrs,function (index) {
                if (!index ){ //首行
                    if (visibleChildLis === 0){　//
                        if (parentColIndex !== undefined){ //有子
                            $(this).find('td').eq(parentColIndex).addClass('layui-hide');
                        }else {　// 无子 即第行跨列了
                            $(this).find('td[columnname="'+columnname+'"]').toggleClass('layui-hide');
                        }
                    }else {
                        $(this).find('td').eq(parentColIndex).attr('colspan',visibleChildLis).removeClass('layui-hide');
                       // $(this).find('td[columnname="'+columnname+'"]').attr('colspan',visibleChildLis).removeClass('layui-hide');
                    }
                }else {　//非首行
                    if (parentColIndex !== undefined){ //
                        $(this).find('>td[columnname="'+columnname+'"]').toggleClass('layui-hide');
                    }
                }
            })

            var bodyColIndex = $(this).data('bodyindex')
            if (bodyColIndex){ //如果设置了bodyindex的，说明表格跨行了，body的列索引要更新
                colIndex = bodyColIndex;
            }
            bt.each(bodyTrs,function (index,tr) {
                var targetCell = $(this).find('td').eq(colIndex);
                targetCell.toggleClass('layui-hide');
            })
            e.stopPropagation();
        })
    }
    //显示所有列信息
    LigerTreeTable.prototype.updateVisibleColumns = function(){
        var $treeContainer = $(this.config.newElem);
        //获取选中的值
        var checkedLis = $treeContainer.parent().find(".tree-table-operate .cols-body") .find(".active");
        var bodyTrs =  $treeContainer.find('.l-grid-body2').find('tr');

        bt.each(checkedLis,function () {
            var bodyColIndex = $(this).data('bodyindex');
            var colIndex = bodyColIndex ? bodyColIndex :$(this).data('col');
            bt.each(bodyTrs,function () {
                $(this).find('td').eq(colIndex).addClass('layui-hide')
            })
        })
    }

    //创建随机数
    LigerTreeTable.prototype.randomNum = function(){
        var num = Math.random()*1000000;
        num = parseInt(num, 10);
        return num;
    }
    //获取数据
    LigerTreeTable.prototype.getData = function () {
        if (this.currentObject && this.currentObject.ligerObj){
            this.currentObject.ligerObj.getData();
        }
    }
    //加载重载数据
    LigerTreeTable.prototype.loadData = function () {
        if (this.currentObject && this.currentObject.ligerObj){
            this.currentObject.ligerObj.loadData();
        }
    }

    //获取指定树表格的参数
    LigerTreeTable.prototype.getConfig = function () {
        if (this.currentObject && this.currentObject.ligerObj){
            return this.currentObject.config
        }
    }

    var treeTable = new LigerTreeTable();
    var obj = {
        init:function(ops,isMultiTree){ // isMultiTree 代表页面上有多树表格
            if(isMultiTree){
                var treeTable1 = new LigerTreeTable();
                treeTable1.set(ops);
                treeTable1.render(isMultiTree);
            }else{
                treeTable.set(ops);
                treeTable.render();
            }
            return this;
        },
        init2:function(ops){
            treeTable.set(ops);
            treeTable.render2();
            return this;
        },
        get:function (bizName) {
            try {
                treeTable.currentObject = window.biz['ligerTree'][bizName];
                return  treeTable;
                // return window.biz['ligerTree'][bizName].ligerObj;
            }catch (e) {
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
        }
    }
    exports('toLigerTree',obj)
})

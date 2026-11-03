'use strict';
avalon.component('assignColumn', {
    template: heredoc(function () {
        /*
         <div class="widget-assign-column">
             <div class="main">
                 <div class="row"  style="border-top:none;">
                     <div class="head">
                        <h4><span class="title" title="所占宽度">所占宽度</span>：</h4>
                     </div>
                     <div class="body">
                         <div class="items">
                             <a href="javascript:void(0);" class="item" :for="width in @widths" :class="[@width.selected?'selected':'']" :click="setConditionSelected(@widths,width)">
                             {{width.name}}
                             </a>
                         </div>
                     </div>
                 </div>
                 <div class="row" >
                     <div class="head">
                        <h4><span class="title" title="所占高度">所占高度</span>：</h4>
                     </div>
                     <div class="body">
                         <div class="items">
                             <a href="javascript:void(0);" class="item" :for="height in @heights" :class="[@height.selected?'selected':'']" :click="setConditionSelected(@heights,height)">
                             {{height.name}}
                             </a>
                         </div>
                     </div>
                 </div>
             </div>
             <div class="main-widget">
                 <div class="layui-fluid" style="padding:0;">
                     <div class="layui-row layui-col-space15">
                         <div class="layui-col-xs3" :for="data in @dataList.data">
                             <div :class="@data.selected?'widget-item-selected':'widget-item'">
                                 <div class="content-detail">
                                     <div>组件名称：{{data.name}}</div>
                                     <div>适配宽度：{{data.minWidth}}等份 - {{data.maxWidth}}等份</div>
                                     <div>建议高度：{{data.height}}像素</div>
                                 </div>

                                 <div class="select-widget no-suit-condition" :visible="!checkColumnWidth(@data)">宽度不满足，无法选择</div>
                                 <div class="select-widget" :visible="checkColumnWidth(@data)" :click="setColumn(@dataList,@data)">
                                     <span :visible="!@data.selected">选择</span>
                                     <span :visible="@data.selected">已选择</span>
                                 </div>
                             </div>
                         </div>
                     </div>
                 </div>
             </div>
         </div>
         */
    }),
    defaults: {
        show:false,
        column:{},
        types:[],
        widths:[],
        heights:[],
        dataList:[],
        sendData:{
            pageSize:10,
            pageNumber:1
        },
        widgetRefreshWatch:{},
        checkColumnWidth:function(data){
            var that = this;
            return that.column.column>=data.minWidth && that.column.column <=data.maxWidth;
        },
        setColumn:function(dataList,data){
            for(var k in dataList.data){
                dataList.data[k].selected = false;
            }
            data.selected = true;
            this.column.name = data.name;
            this.column.showHead = (typeof(data.showHead) === "undefined")?true:data.showHead;
            this.column.widgetName = data.widgetName;
            this.column.content = data.content;
            this.column.height = data.height;
        },
        setConditionSelected:function(els,el){
            for(var k in els){
                els[k].selected = false;
            }
            el.selected = true;
            this.getData();
        },
        getSearchSelectedParam:function(param,values,field){
            for(var k in values){
                if(values[k].selected){
                    param[field] = values[k].code;
                    break;
                }
            }
        },
        getData:function(){
            var that = this;
            var param = {};
            that.getSearchSelectedParam(param,that.types,'type');
            that.getSearchSelectedParam(param,that.widths,'width');
            that.getSearchSelectedParam(param,that.heights,'height');
            param = $.extend(true, that.sendData.$model,param);
            $.ajax({
                type:"POST",
                url: basePath + "/sys/homeWidget/list",
                dataType:"json",
                contentType:"application/json",
                data:JSON.stringify(param),
                success:function(result){
                    if(result){
                        that.sendData.pageNumber = result.pageNumber;
                        that.sendData.pageSize = result.pageSize;
                        var temp = [];
                        var data =  result.data;
                        for(var k in data){
                            data[k].selected = that.column.type === data[k].type && that.column.content === data[k].content;
                            temp.push(data[k]);
                        }
                        result.data = temp;
                        that.dataList = result;
                    }
                }
            });
        },
        onReady:function(e){
            var that = this;
            that.widgetRefreshWatch = e.vmodel.$watch('show', function(a){
                if(!a){
                    return;
                }
                $.get(basePath+"/sys/homeWidget/condition", function(data) {
                    that.widths = that.reduceDataField(data.width);
                    that.heights = that.reduceDataField(data.height);
                },'JSON');
                that.getData();
            });
        },
        onDispose:function(){
            //组件销毁时，注销监听
            this.widgetRefreshWatch();
        },
        reduceDataField:function(data){
            var temp = [];
            temp.push({name:"全部",code:''.code,selected:true});
            for(var k in data){
                var item = data[k];
                temp.push({name:item.name,code:item.code,selected:false});
            }
            return temp;
        }
    }
});
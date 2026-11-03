'use strict';
avalon.component('widgetLoading', {
    template: heredoc(function () {
        /*
         <div class="component-content-body-on-refresh"
             :if="@column.refresh"
             :css="{height:@column.height?@column.height:'200px',lineHeight:@column.height?@column.height+'px':'200px'}">
             <div class="reloading">
                <img src="static/images/loading.gif">
             </div>
         </div>
         */
    }),
    defaults: {
        column:{},
        onInit:function(e){
            if(bt.widgetLoading){
                var model = e.vmodel;
                bt.widgetLoading[model.$id]=model;
            }
        }
    }
});
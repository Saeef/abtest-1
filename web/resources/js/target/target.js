/**
 * Created by Administrator on 2015/5/21.
 */
var addwin=null,alertwin  = null,editwin = null,delwin = null,statuswin = null,targetwin = null,fectorwin = null,userinfoWin = null,conditions,tarCons,userGroups;
var proTable,tarTable,facTable;
$.ajax({
    type: "GET",
    url: "/conditionCtl/getConditions",
    async:true,
    success: function (resp) {
        resp = eval("(" + resp + ")");
        conditions = resp.conditions;
        $.each(conditions,function(i,condition){
            if(typeof(condition.showcondition)!="undefined"){
                var conid = condition.showcondition;
                var showElemId= condition.name;
                var type = condition.type;
                $.each(conditions,function(i,condition){
                    if(condition.id==conid){
                        $("#"+condition.name).change(function(){
                            if($(this).is(":checked")){
                                $("#div"+showElemId).show();
                            }else{
                                //hide
                                $("#div"+showElemId).hide();
                            }
                        });
                    }
                });
            }
        });
    }
});
$.ajax({
    type: "GET",
    url: "/conditionCtl/getConditionValue",
    async:true,
    success: function (resp) {
        var resps = eval("(" + resp + ")");
        userGroups = resps.checkboxs.User;
        var multis = resps.multiselects;
        var selects = resps.selects;
        $.each(multis,function(i,idNames){
            var html = '';
            $.each(idNames,function(i,idName){
                html+='<option value="'+idName.id+'">'+idName.name+'</option>';
            });
            if(html!=''){
                $('#'+i).html(html);
                //$('#rate'+i).html(html);
                $('#'+i).multiselect().multiselectfilter();
                //$('#rate'+i).multiselect().multiselectfilter();
            }
        });
        $.each(selects,function(i,selectIdNames){
            var html = '';
            $.each(selectIdNames,function(i,selectIdName){
                html+='<option value="'+selectIdName.id+'">'+selectIdName.name+'</option>';
            });
            if(html!=''){
                $('#'+i).html(html);
            }
        });
    }
});
function editPro(id, name, desc,status){
    if(editwin==null){
        editwin = dialog({
            title: 'Edit project ['+name+']',
            content: proForm,
            okValue: 'Confirm',
            ok: function () {
                var proname =ignoreSpaces($('#proname').val());
                var prodesc =trim($('#prodesc').val());
                var status = $('#status').val();
                if(proname==''||!isNaN(proname)){
                    artAlert('Project name is not null or number.');
                    return false;
                }
                if(prodesc==''){
                    artAlert('Project description is not null.');
                    return false;
                }
                $.ajax({
                    type: "GET",
                    url: "/project/edit",
                    async:false,
                    data:{id:id,proname:proname,prodesc:prodesc,status:status,method:"edit"},
                    success: function (resp) {
                        resp = eval("("+resp+")");
                        if(resp.success == true){
                            artAlert(resp.msg);
                            proTable.fnClearTable(0); //clear data
                            proTable.fnDraw(); //refresh data
                            editwin.close();
                            editwin=null;
                        }else{
                            artAlert(resp.msg);
                        }
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {editwin.close();editwin=null;}
        });
    }
    editwin.showModal();
    $('#proname').val(name);
    $('#prodesc').val(desc);
    $('#status').val(status=='active'?0:1);
}

function deletePro(id){
    delwin = dialog({
        title: 'Advice',
        content: 'Sure you want to delete?',
        okValue: 'Confirm',
        ok: function () {
            $.ajax({
                type: "GET",
                url: "/project/edit",
                async:false,
                data:{id:id,method:"delete"},
                success: function (resp) {
                    resp = eval("("+resp+")");
                    if(resp.success == true){
                        artAlert(resp.msg);
                        proTable.fnClearTable(0); //clear data
                        proTable.fnDraw(); //refresh data
                        delwin.close();delwin=null;
                    }else{
                        artAlert(resp.msg);
                    }
                }
            });
        },
        cancelValue: 'Cancel',
        cancel: function () {delwin.close();delwin=null;}
    });
    delwin.showModal();
}
function changePro(id,status){
    statuswin = dialog({
        title: 'Advice',
        content: 'Sure you want to Change Project Status?',
        okValue: 'Confirm',
        ok: function () {
            $.ajax({
                type: "GET",
                url: "/project/edit",
                async:false,
                data:{id:id,status:(status=='active'?0:1),method:'update'},
                success: function (resp) {
                    resp = eval("("+resp+")");
                    if(resp.success == true){
                        proTable.fnClearTable(0); //clear data
                        proTable.fnDraw(); //refresh data
                    }
                    statuswin.close();
                    statuswin=null;
                    artAlert(resp.msg);

                }
            });
        },
        cancelValue: 'Cancel',
        cancel: function () {statuswin.close();statuswin=null;}
    });
    statuswin.showModal();
}

//*******************************************target list*************************************************
function getTarget(id, proname,status){
    var keyword = $("#targetKeyword").val();
    if(targetwin==null) {
        targetwin = dialog({title: 'Targets Of ' + proname,width:1200});
    }
    var html = '<div id="target_data">';
            if(status=='inactive'){
                html+= '<div class="row">'+
                '<div class="col-md-14">'+
                '<div class="alert alert-danger">'+
                '<strong>Parent Project is inactive.</strong> The Follow Target is invalid.Please Change parent project.'+
                '</div>'+
                '</div>'+
                '</div>';
            }

            html+='<a href="javascript:void(0)" onclick="showTargetForm('+id+')" class="btn btn-secondary btn-sm btn-icon icon-left" data-toggle="modal" data-target="#myModal">'+
                    'Add Target'+
                    '</a>'+
                    '<table width="100%" class="table table-striped table-bordered" border="0" cellspacing="0" cellpadding="0" id="t_target_data">'+
                     '<thead>'+
                     '</thead>'+
                     '<tfoot>'+
                     '</tfoot>'+
                    '</table>'+
                    '</div>';
    targetwin.content(html);
    targetwin.addEventListener('close', function(){targetwin=null;});
    targetwin.showModal();
    getTargetDataTable(id);
}
function getTargetDataTable(proid){
	if (tarTable == undefined || proTable == "undefined" || tarTable == null) {
    }else{
    	tarTable.fnDestroy();
    }
    tarTable = $('#t_target_data').dataTable({
        "bPaginate" : true,
        "bLengthChange" : true,
        "processing": true,
        "bDestroy": true, 
        "bRetrieve": true,
        "bProcessing": true,
        bFilter:true,
        aLengthMenu: [
            [5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "All"]
        ],
        "oLanguage": {
			"sProcessing": "<div style='margin-left:10px;'><img src='/resources/images/loading.gif'></div>"
		},
        "sPaginationType": "full_numbers",
        "bServerSide" : true,
        "sAjaxSource" : "/target/list?rand="+Math.random(),
        "fnServerData" : function(sSource, aoData, fnCallback) {
            $.ajax({
                "type" : "get",
                "url" : sSource,
                "dataType" : "json",
                "data" : {
                    aoData : JSON.stringify(aoData),
                    "type" : 5,
                    projectId:proid
                },
                "success" : function(resp) {
                    fnCallback(resp);
                }
            });
        },
        "aoColumns": [
            {"sTitle": "Name" , "bSortable": true,"bVisible": true,"sWidth": "15%",  "mData": function(source, type, val){
                var targetName=source.name;
                var html = '<p title="'+targetName+'">'+targetName+'</p>';
                if(targetName.length>15){
                    html = '<p title="'+targetName+'">'+targetName.substring(0,15)+'...</p>';
                }
                if(source.type=='AB-Test'){
                    html='<a title="'+targetName+'" href="javascript:void(0);" onclick="getFactor('+source.id+',\''+targetName+'\',\''+source.active+'\')">'+targetName+'</a>';
                    if(targetName.length>15){
                        html='<a title="'+targetName+'" href="javascript:void(0);" onclick="getFactor('+source.id+',\''+source.name+'\',\''+source.active+'\')">'+targetName.substring(0,15)+'...</a>';
                    }

                }
                return html;
            }},
            {"sTitle": "Description", "bSortable": false,"sWidth": "8%","mData": function(source,type,val){
                var description = source.description;
                var html = '<p title="'+description+'">'+description+'</p>';
                if(description.length>15){
                    html = '<p title="'+description+'">'+description.substring(0,15)+'...</a>';
                }
                return html;
            }},//, "bVisible": false},
            {"sTitle": "type" ,"bSortable": true,"sWidth": "15%","mData": "type"},
            {"sTitle": "Status" ,"bSortable": true,"sWidth": "15%","mData": "active"},
            {"sTitle": "Createtime" ,"bSortable": true,"sWidth": "15%","mData": "createtime"},
            {"sTitle": "Operation" ,"bSortable": false,"sWidth": "20%","mData": function(source, type, val){
                var html='';
                html+='<a href="javascript:void(0);" class="btn btn-secondary btn-sm btn-icon icon-left" onclick="editTarget('+source.id+',\''+source.name+'\',\''+source.description+'\',\''+source.type+'\',\''+source.active+'\','+source.countryControl+','+source.pageControl+')">'+
                'Edit'+
                '</a>'+
                '<a href="javascript:void(0);" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delTarget('+source.id+')">'+
                'Delete'+
                '</a>'+
                '<a href="javascript:void(0);" class="btn btn-info btn-sm btn-icon icon-left" onclick="updateStatus('+source.id+', \''+source.active+'\')">'+
                (source.active=='active'?'inactive':'active')+
                '</a>';
                return html;
            }}
           ]
    }).yadcf([
//        {column_number : 0, filter_type: 'text'},
		{column_number : 2},
        {column_number : 3},
        {column_number : 4, filter_type: 'text'}
    ]);
}
function editTarget(id, name, desc, type, status, country, pageid){
    changeCondition(type=='AB-Test'?1:0);
    $.each(conditions,function(i,condition){
        if(condition.type=='checkbox'){
            $("#"+condition.name).attr("checked",false);
        }else if(condition.type=='select'&&condition.name=='GroupBy'){
            $("#"+condition.name+" option:first").prop("selected", 'selected');
        }else if(condition.type=='multiselect'){
            $("#"+condition.name).multiselect('uncheckAll');
            if(typeof(condition.showcondition)!="undefined"){
                $("#div"+condition.name).hide();
            }
        }

    });
    if(type=='AB-Test'){
        $.ajax({
            type: "GET",
            url: "/target/getConByTargetId",
            async:true,
            data:{targetid:id},
            success: function (resp) {
                resp = eval("(" + resp + ")");
                tarCons = resp.conditions;
                //init the condition value.
                if(tarCons.length>0){
                    $.each(conditions,function(i,condition){
                        var ids=[];
                        var conName=condition.name;
                        $.each(tarCons,function(i,tarCon){
                            if(tarCon.type=='checkbox'){
                                document.getElementById(tarCon.name).checked = true;
                                if(typeof(condition.showcondition)!="undefined"){
                                    var conid = condition.showcondition;
                                    var showElemId= condition.name;
                                    //var type = condition.type;
                                    if(tarCon.id==conid){
                                        $("#div"+showElemId).show();
                                    }
                                }
                            }else if(tarCon.type=='selectvalue'&&tarCon.ptype=='multiselect'){
                                if(condition.id==tarCon.pid){
                                    ids.push(tarCon.condition_id);
                                }
                            }else if(tarCon.type=='selectvalue'&&tarCon.ptype=='select'){
                                $("#"+conName).val(tarCon.condition_id);
                            }
                        });
                        if(ids.length>0){
                            $("#"+conName).val(ids);
                            $("#"+conName).multiselect("refresh");
                        }else{
                            if(condition.type=='multiselect'){
                                $("#"+conName).multiselect('uncheckAll');
                            }
                        }
                    });
                }

            }
        });
    }

    if(editwin==null){
        editwin = dialog({
            title: 'Advice',
            content: addTargetForm,
            okValue: 'Confirm',
            ok: function () {
                var modifiedName =ignoreSpaces($('#targetname').val());
                var modifiedDesc =trim($('#targetdesc').val());
                var modifiedStatus = $('#tarstatus').val();
                var modifiedType = $('#targettype').val();
                if(modifiedName==''||!isNaN(modifiedName)){
                    artAlert('Target name can not be null or number.');
                    return false;
                }
                if(modifiedDesc==''){
                    artAlert('Target description can not be null.');
                    return false;
                }
                var modifiedCountry = 0;
                if( $("#country").is(":checked")){
                    modifiedCountry = 1;
                }else{
                    modifiedCountry = 0;
                }
                var modifiedPage = 0;
                if( $("#pageid").is(":checked")){
                    modifiedPage = 1;
                }else{
                    modifiedPage = 0;
                }
                //if rate then control the following conditions must be checked
                if( $('#IsRate').length>0&&$("#IsRate").is(":checked")){
                    if( $('#Device').length>0){
                        var selects = $('#Device').val();
                        if(selects==null||selects.length==0){
                            artAlert('Please choose Device.');
                            return false;
                        }
                    }
                    if( $('#ABTestCountry').length>0){
                        var selects = $('#ABTestCountry').val();
                        if(selects==null||selects.length==0){
                            artAlert('Please choose ABTestCountry.');
                            return false;
                        }
                    }
                    if( $('#PagePath').length>0) {
                        var selects = $('#PagePath').val();
                        if (selects == null || selects.length == 0) {
                            artAlert('Please choose PagePath.');
                            return false;
                        }
                    }
                }
                var data={id:id,name:modifiedName,desc:modifiedDesc,type:modifiedType,status:modifiedStatus,country:modifiedCountry,pageid:modifiedPage,method:'edit'};
                if(modifiedType==1&&$('#IsRate').length>0&&$("#IsRate").is(":checked")){
                    $.each(conditions,function(i,condition){
                        if(condition.type=='checkbox'){
                            if( $("#"+condition.name).is(":checked")){
                                data[condition.name] = condition.id;
                            }
                        }else if(condition.type=='multiselect'){
                            var selects = $('#'+condition.name).val();
                            var values='';
                            if(selects!=null&&selects.length!=0){
                                $.each(selects,function(i,select){
                                    values+=select+',';
                                });
                                values = values.substring(0,values.length-1);
                            }
                            data[condition.name]=values;
                        }else if(condition.type=='select'){
                            var value = $('#'+condition.name).val();
                            data[condition.name]=value;
                        }
                    });
                }
                $.ajax({
                    type: "GET",
                    url: "/target/edit",
                    async:false,
                    data:data,
                    success: function (resp) {
                        resp = eval("(" + resp + ")");
                        if(resp.success){
                        	tarTable.fnClearTable(0); //clear table data
                        	tarTable.fnDraw(); //reload table
                        }
                        editwin.close();
                        editwin=null;
                        artAlert(resp.msg);
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {editwin.close();editwin=null;}
        });
    }
    editwin.showModal();
    $('#targetname').val(name);
    $('#targettype').val(type=='AB-Test'?1:0);
    $('#tarstatus').val(status=='active'?0:1);
    if(country == 1){
        document.getElementById("country").checked = true;
    }else{
        document.getElementById("country").checked = false;
    }
    if(pageid == 1){
        document.getElementById("pageid").checked = true;
    }else{
        document.getElementById("pageid").checked = false;
    }
    $('#targetdesc').val(desc);
}

function delTarget(id){
    delwin = dialog({
        title: 'Advice',
        content: 'Sure you want to delete?',
        okValue: 'Confirm',
        ok: function () {
            $.ajax({
                type: "GET",
                url: "/target/edit",
                async:false,
                data:{id:id,method:'delete'},
                success: function (resp) {
                    resp = eval("(" + resp + ")");
                    if(resp.success){
                    	tarTable.fnClearTable(0); //clear table data
                    	tarTable.fnDraw(); //reload table
                    }
                    delwin.close();delwin=null;
                    artAlert(resp.msg);
                }
            });
        },
        cancelValue: 'Cancel',
        cancel: function () {delwin.close();delwin=null;}
    });
    delwin.showModal();
}

function updateStatus(id,status){
    statuswin = dialog({
        title: 'Advice',
        content: 'Sure you want to Change Project Status?',
        okValue: 'Confirm',
        ok: function () {
            $.ajax({
                type: "GET",
                url: "/target/edit",
                async:false,
                data:{id:id,status:(status=="active"?0:1),method:'update'},
                success: function (resp) {
                    resp = eval("(" + resp + ")");
                    if(resp.success){
                    	tarTable.fnClearTable(0); //clear table data
                    	tarTable.fnDraw(); //reload table
                    }
                    statuswin.close();statuswin=null;
                    artAlert(resp.msg);
                }
            });
        },
        cancelValue: 'Cancel',
        cancel: function () {statuswin.close();statuswin=null;}
    });
    statuswin.showModal();
}

//************************************************************************************************************************************************************


function artAlert(msg){
    if(alertwin==null){
        alertwin = dialog({
            title: 'Advice',
            content: msg,
            cancelValue: 'Cancel',
            cancel: function () {alertwin.close();alertwin=null;}
        });
    }
    alertwin.showModal();
}
function showProForm(){
    if(addwin==null){
        addwin = dialog({
            title: 'Project',
            content: proForm,
            okValue: 'Confirm',
            ok: function () {
                var proname =ignoreSpaces($('#proname').val());
                var prodesc =trim($('#prodesc').val());
                var status = $('#status').val();
                if(proname==''||!isNaN(proname)){
                    artAlert('Project name is not null or number.');
                    return false;
                }
                if(prodesc==''){
                    artAlert('Project description is not null.');
                    return false;
                }
                if(checkNameIsExist("project",proname)){
                    artAlert('Project name is already existed.');
                    return false;
                }
                $.ajax({
                    type: "GET",
                    url: "/project/add",
                    async:false,
                    data:{proname:proname,prodesc:prodesc,status:status},
                    success: function (result) {
                        result = eval("("+result+")");
                        if(result.success == true){
                            artAlert(result.msg);
                            proTable.fnClearTable(0); //clear data
                            proTable.fnDraw(); //refresh datatable
                            addwin.close();addwin=null;
                        }else{
                            artAlert(result.msg);
                        }
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {addwin.close();addwin=null;}
        });
    }
    $('#proname').val('');
    $('#prodesc').val('');
    $("select option[value='"+0+"']").attr("selected", "selected");
    addwin.showModal();
}
function checkNameIsExist(type,nameVal){
    var flag = false;
    $.ajax({
        type: "GET",
        url: "/"+type+"/checkNameIsExist",
        async:false,
        data:{name:nameVal},
        success: function (resp) {
            resp = eval("(" + resp + ")");
            if(resp.isexist){
                flag = true;
            }
        }
    });
    return flag;
}
function showTargetForm(projectid){
    changeCondition(1);
    $.each(conditions,function(i,condition){
        if(condition.type=='checkbox'){
            $("#"+condition.name).attr("checked",false);
        }else if(condition.type=='select'&&condition.name=='GroupBy'){
            $("#"+condition.name+" option:first").prop("selected", 'selected');
        }else if(condition.type=='multiselect'){
            $("#"+condition.name).multiselect('uncheckAll');
            if(typeof(condition.showcondition)!="undefined"){
                $("#div"+condition.name).hide();
            }
        }

    });
    if(addwin==null){
        addwin = dialog({
            title: 'Target',
            content: addTargetForm,
            okValue: 'Confirm',
            ok: function () {
                var name =ignoreSpaces($('#targetname').val());
                var type =trim($('#targettype').val());
                var country = 0;
                if( $("#country").is(":checked")){
                    country = 1;
                }else{
                    country = 0;
                }
                var pageid = 0;
                if( $("#pageid").is(":checked")){
                    pageid = 1;
                }else{
                    pageid = 0;
                }
                var desc =trim($('#targetdesc').val());
                var status = $('#tarstatus').val();

                if(name==''||!isNaN(name)){
                    artAlert('Target name is not null or number.');
                    return false;
                }
                if(desc==''){
                    artAlert('Target description is not null.');
                    return false;
                }
                //if rate then control the following conditions must be checked
                if( $('#IsRate').length>0&&$("#IsRate").is(":checked")){
                    if( $('#Device').length>0){
                        var selects = $('#Device').val();
                        if(selects==null||selects.length==0){
                            artAlert('Please choose Device.');
                            return false;
                        }
                    }
                    if( $('#ABTestCountry').length>0){
                        var selects = $('#ABTestCountry').val();
                        if(selects==null||selects.length==0){
                            artAlert('Please choose ABTestCountry.');
                            return false;
                        }
                    }
                    if( $('#PagePath').length>0) {
                        var selects = $('#PagePath').val();
                        if (selects == null || selects.length == 0) {
                            artAlert('Please choose PagePath.');
                            return false;
                        }
                    }
                }
                if(checkNameIsExist("target",name)){
                    artAlert('Target name is already existed.');
                    return false;
                }

                var data={projectid:projectid,name:name,type:type,country:country,pageid:pageid,desc:desc,status:status};
                // if rate abtest then add the conditions to request
                if(type==1&&$('#IsRate').length>0&&$("#IsRate").is(":checked")){//abtest
                    $.each(conditions,function(i,condition){
                        if(condition.type=='checkbox'){
                            if( $("#"+condition.name).is(":checked")){
                                data[condition.name] = condition.id;
                            }
                        }else if(condition.type=='multiselect'){
                            var selects = $('#'+condition.name).val();
                            var values='';
                            if(selects!=null&&selects.length!=0){
                                $.each(selects,function(i,select){
                                    values+=select+',';
                                });
                                values = values.substring(0,values.length-1);
                            }
                            data[condition.name]=values;
                        }else if(condition.type=='select'){
                            var value = $('#'+condition.name).val();
                            data[condition.name]=value;
                        }
                    });
                }

                $.ajax({
                    type: "GET",
                    url: "/target/add",
                    async:false,
                    data:data,
                    success: function (resp) {
                        resp = eval("(" + resp + ")");
                        if(resp.success){
                        	tarTable.fnClearTable(0); //clear table data
                        	tarTable.fnDraw(); //reload table
                        }
                        addwin.close();addwin=null;
                        artAlert(resp.msg);
                    },
                    failure:function(){
                        addwin.close();addwin=null;
                        artAlert('add failed!');
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {addwin.close();addwin=null;}
        });
    }
    $('#targetname').val('');
    $('#targetdesc').val('');
    $("#country").attr("checked",false);
    $("#pageid").attr("checked",false);
    $.each(conditions,function(i,condition){
        $("#"+condition.name).attr("checked",false);
        //document.getElementById(condition.name).checked=false;
    });
    //$("select option[value='"+0+"']").attr("selected", "selected");
    $('#targettype').val(1);
    $('#tarstatus').val(0);
    addwin.showModal();
}

function getFactor(targetid,targetname,status){
    var keyword = $("#factorKeyword").val();
    if(fectorwin==null) {
        fectorwin = dialog({
            title: 'Factors Of ' + targetname,width:1200});
    }
    var html = '<div id="factor_data">';
            if(status=='inactive'){
                html+= '<div class="row">'+
                '<div class="col-md-14">'+
                '<div class="alert alert-danger">'+
                '<strong>Parent Target is inactive.</strong> The Follow Factors is invalid.Please Change parent target.'+
                '</div>'+
                '</div>'+
                '</div>';
            }
	    html+='<a href="javascript:void(0)" onclick="showFactorForm('+targetid+')" class="btn btn-secondary btn-sm btn-icon icon-left" data-toggle="modal" data-target="#myModal">'+
                'Add Factor'+
            '</a>'+
            '<table width="100%" class="table table-striped table-bordered" border="0" cellspacing="0" cellpadding="0" id="t_factor_data">'+
             '<thead>'+
             '</thead>'+

             '<tfoot>'+
             '</tfoot>'+
            '</table>'+
            '</div>';

    fectorwin.content(html);
    fectorwin.addEventListener('close', function(){fectorwin=null;});
    fectorwin.showModal();
    getFactorDataTable(targetid);
}
function getFactorDataTable(tarid){
	if (facTable == undefined || facTable == "undefined" || facTable == null) {
    }else{
    	facTable.fnDestroy();
    }
	facTable = $('#t_factor_data').dataTable({
        "bPaginate" : true,
        "bLengthChange" : true,
        "processing": true,
        "bDestroy": true,
        "bRetrieve": true,
        "bProcessing": true,
        bFilter:true,
        aLengthMenu: [
            [5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "All"]
        ],
        "oLanguage": {
			"sProcessing": "<div style='margin-left:10px;'><img src='/resources/images/loading.gif'></div>"
		},
        "sPaginationType": "full_numbers",
        "bServerSide" : true,
        "sAjaxSource" : "/factor/list?rand="+Math.random(),
        "fnServerData" : function(sSource, aoData, fnCallback) {
            $.ajax({
                "type" : "get",
                "url" : sSource,
                "dataType" : "json",
                "data" : {
                    aoData : JSON.stringify(aoData),
                    "type" : 5,
                    targetId:tarid
                },
                "success" : function(resp) {
                    fnCallback(resp);
                }
            });
        },
        "aoColumns": [
            {"sTitle": "Name" , "bSortable": true,"bVisible": true,"sWidth": "20%",  "mData": function(source,type,val){
                var name = source.name;
                var html = '<p title="'+name+'">'+name+'</p>';
                if(name.length>20){
                    html = '<p title="'+name+'">'+name.substring(0,20)+'...</a>';
                }
                return html;
            }},
            {"sTitle": "Description", "bSortable": false,"sWidth": "20%","mData": function(source,type,val){
                var description = source.description;
                var html = '<p title="'+description+'">'+description+'</p>';
                if(description.length>40){
                    html = '<p title="'+description+'">'+description.substring(0,40)+'...</a>';
                }
                return html;
            }},//, "bVisible": false},
            {"sTitle": "Createtime" ,"bSortable": true,"sWidth": "15%","mData": "createtime"},
            {"sTitle": "Operation" ,"bSortable": false,"sWidth": "20%","mData": function(source, type, val){
                var html='';
                html+='<a href="javascript:void(0);" class="btn btn-secondary btn-sm btn-icon icon-left" onclick="editFactor('+source.targetId+','+source.id+',\''+source.name+'\',\''+source.description+'\')">'+
                'Edit'+
                '</a>'+
                '<a href="javascript:void(0);" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delFactor('+source.id+')">'+
                'Delete'+
                '</a>';
                return html;
            }}
           ]
    }).yadcf([
        {column_number : 2, filter_type: 'text'}
    ]);;
}
function editFactor(targetid,id, name, desc){
    var facCons =null;
    $("#factorcon").empty();
    if(addwin==null){
        addwin = dialog({
            title: 'Factor',
            content: addFactorForm,
            okValue: 'Confirm',
            ok: function () {
                var modifiedName =ignoreSpaces($('#factorname').val());
                var modifiedDesc =trim($('#factordesc').val());
                if(modifiedName==''||!isNaN(modifiedName)){
                    artAlert('Factor name is not null or number.');
                    return false;
                }
                if(modifiedDesc==''){
                    artAlert('Factor description is not null.');
                    return false;
                }
                var data={id:id,name:modifiedName,desc:modifiedDesc,method:"edit"};
                if(tarCons!=null){
                    $.each(tarCons,function(i,tarCon){
                        if(tarCon.name=='User'){
                            if($('#fac'+tarCon.name).length!=0){
                                var selects = $('#fac'+tarCon.name).val();
                                var values='';
                                if(selects!=null&&selects.length!=0){
                                    $.each(selects,function(i,select){
                                        values+=select+',';
                                    });
                                    values = values.substring(0,values.length-1);
                                }
                                data["fac"+tarCon.name]=values;
                            }
                        }
                    });
                }
                $.ajax({
                    type: "GET",
                    url: "/factor/edit",
                    async:false,
                    data:data,
                    success: function (resp) {
                        resp = eval("(" + resp + ")");
                        if(resp.success){
                        	facTable.fnClearTable(0); //clear table data
                        	facTable.fnDraw(); //reload table
                        }
                        addwin.close();addwin=null;
                        artAlert(resp.msg);
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {addwin.close();addwin=null;}
        });
    }
    $('#factorname').val(name);
    $('#factordesc').val(desc);
    $.ajax({
        type: "GET",
        url: "/target/getConByTargetId",
        async:false,
        data:{targetid:targetid},
        success: function (resp) {
            resp = eval("(" + resp + ")");
            tarCons = resp.conditions;
        }
    });
    //add target condition to div container.
    if(typeof(tarCons)!='undefined'&&tarCons.length>0){
        $.each(tarCons,function(i,tarCon){
            var conName = tarCon.name;
            var type = tarCon.type;
            if(conName=='User'){
                if($("#faclabel"+conName).length==0&&$("#fac"+conName).length==0){
                    var options='';
                    $.each(userGroups,function(i,userGroup){
                        options+="<option value='"+userGroup.id+"'>"+userGroup.name+"</option>"
                    });
                    var link = $("<label for='fac"+conName+"' id='faclabel"+conName+"' style='width: 100px;text-align: right;'>"+conName+":</label><select id='fac"+conName+"' multiple='multiple'>"+options+"</select>");
                    $("#factorcon").append(link);
                    $('#fac'+conName).multiselect().multiselectfilter();
                }
            }
        });
    }
    $.ajax({
        type: "GET",
        url: "/factor/getConValueByFactorId",
        async:true,
        data:{factorid:id},
        success: function (resp) {
            resp = eval("(" + resp + ")");
            facCons = resp.conditionValues;
            $.each(facCons,function(i,facCon){
                var conName = facCon.name;
                if(conName=='User'){
                    var ids=$("#fac"+conName).length==0?'':$("#fac"+conName).val();
                    if($("#faclabel"+conName).length==0&&$("#fac"+conName).length==0){
                        $.each(userGroups,function(i,userGroup){
                            if(facCon.id==userGroup.id){
                                ids += userGroup.id+',';
                            }
                        });
                        ids = ids.substring(0,ids.length-1);
                        var idarr = ids.split(",");
                        if(idarr.length>0){
                            $("#fac"+conName).val(idarr);
                            $("#fac"+conName).multiselect("refresh");
                        }else{
                            $("#fac"+conName).multiselect('uncheckAll');
                        }
                    }else{
                        $.each(userGroups,function(i,userGroup){
                            if(facCon.id==userGroup.id){
                                ids += ','+userGroup.id;
                            }
                        });
                        $('#fac'+conName).multiselect().multiselectfilter();
                        var idarr = ids.split(",");
                        if(idarr.length>0){
                            $("#fac"+conName).val(idarr);
                            $("#fac"+conName).multiselect("refresh");
                        }else{
                            $("#fac"+conName).multiselect('uncheckAll');
                        }
                    }
                }
            });
        }
    });
    addwin.showModal();
}

function delFactor(id){
    delwin = dialog({
        title: 'Advice',
        content: 'Sure you want to delete?',
        okValue: 'Confirm',
        ok: function () {
            $.ajax({
                type: "GET",
                url: "/factor/edit",
                async:false,
                data:{id:id,method:"delete"},
                success: function (resp) {
                    resp = eval("(" + resp + ")");
                    if(resp.success){
                    	facTable.fnClearTable(0); //clear table data
                    	facTable.fnDraw(); //reload table
                    }
                    delwin.close();delwin=null;
                    artAlert(resp.msg);
                }
            });
        },
        cancelValue: 'Cancel',
        cancel: function () {delwin.close();delwin=null;}
    });
    delwin.showModal();
}

function showFactorForm(targetid){
    //clear condition div container
    $("#factorcon").empty();
    //load this target condition.
    if(addwin==null){
        addwin = dialog({
            title: 'Factor',
            content: addFactorForm,
            okValue: 'Confirm',
            ok: function () {
                var name =ignoreSpaces($('#factorname').val());
                var desc =trim($('#factordesc').val());
                if(name==''||!isNaN(name)){
                    artAlert('Factor name is not null or number.');
                    return false;
                }
                if(desc==''){
                    artAlert('Factor description is not null.');
                    return false;
                }
                if(checkNameIsExist("factor",name)){
                    artAlert('Factor name is already existed.');
                    return false;
                }
                var data = {targetid:targetid,name:name,desc:desc};
                $.each(tarCons,function(i,tarCon){
                    if(tarCon.name=='User'){
                        if($('#fac'+tarCon.name).length!=0){
                            var selects = $('#fac'+tarCon.name).val();
                            var values='';
                            if(selects!=null&&selects.length!=0){
                                $.each(selects,function(i,select){
                                    values+=select+',';
                                });
                                values = values.substring(0,values.length-1);
                            }
                            data["fac"+tarCon.name]=values;
                        }
                    }
                });
                $.ajax({
                    type: "GET",
                    url: "/factor/add",
                    async:false,
                    data:data,
                    success: function (resp) {
                        resp = eval("(" + resp + ")");
                        if(resp.success){
                        	facTable.fnClearTable(0); //clear table data
                        	facTable.fnDraw(); //reload table
                        }
                        addwin.close();addwin=null;
                        artAlert(resp.msg);
                    }
                });
            },
            cancelValue: 'Cancel',
            cancel: function () {addwin.close();addwin=null;}
        });
    }
    addwin.showModal();
    $.ajax({
        type: "GET",
        url: "/target/getConByTargetId",
        async:false,
        data:{targetid:targetid},
        success: function (resp) {
            resp = eval("(" + resp + ")");
            tarCons = resp.conditions;
        }
    });
    $('#targetId_addFactor').val(targetid);
    $('#factorname').val('');
    $('#factordesc').val('');
    //add target condition to div container.
    if(typeof(tarCons)!='undefined'&&tarCons.length>0){
        $.each(tarCons,function(i,tarCon){
            var conName = tarCon.name;
            var type = tarCon.type;
            if(conName=='User'){
                if($("#faclabel"+conName).length==0&&$("#fac"+conName).length==0){
                    var options='';
                    $.each(userGroups,function(i,userGroup){
                        options+="<option value='"+userGroup.id+"'>"+userGroup.name+"</option>"
                    });
                    var link = $("<label for='fac"+conName+"' id='faclabel"+conName+"' style='width: 100px;text-align: right;'>"+conName+":</label><select id='fac"+conName+"' multiple='multiple'>"+options+"</select>");
                    $("#factorcon").append(link);
                    $('#fac'+conName).multiselect().multiselectfilter();
                }
            }
        });
    }
}
function changeTarget(type,proid){//type 0 tracking 1 abtest
    var typeValue = '';
    if(type==2){
        typeValue = 1;
    }else{
        typeValue = type;
    }
    $.ajax({
        type: "GET",
        url: "/target/getTargetByTypeAndProid",
        async:true,
        data:{type:typeValue,proid:proid},
        success: function (resp) {
            resp = eval("("+resp+")");
            var targets = resp.targetInfo;
            var targethtml = '';
            $.each(targets,function(i,target){
                targethtml+="<option value='"+target.id+"' country='"+target.country+"' pageid='"+target.pageId+"'>"+target.name+"</option>";
            });
            if(type==1){
                if(targethtml==''){
                    $('#abtesttarl').html('<option>select...</option>');
                    var selectBox = $("select#abtesttarl").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    //first remove the selected before.
                    $('li[class=select2-search-choice]').remove();
                    $('#abtestfacl').html('');
                    $('#acountry').html('<option value="0">select...</option>');
                    var selectBox = $("select#acountry").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    $('#apageid').html('<option value="0">select...</option>');
                    var selectBox = $("select#apageid").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                }else{
                    //refresh factor mutilselect.
                    $('li[class=select2-search-choice]').remove();
                    $('#abtestfacl').html('');
                    $('#abtesttarl').html(targethtml);
                    var selectBox = $("select#abtesttarl").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    var firstOption = $('#abtesttarl').find("option").first();
                    var fval = firstOption.val();
                    changeFactor(fval);
                    showCountryAndPageId(fval,firstOption.attr('country'),firstOption.attr('pageid'),'abtest');
                }
            }else if(type==0){
                if(targethtml==''){
                    $('#tracktarl').html('<option>select...</option>');
                    $('#tcountry').html('<option value="0">select...</option>');
                    var selectBox = $("select#tcountry").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    $('#tpageid').html('<option value="0">select...</option>');
                    var selectBox = $("select#tpageid").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                }else{
                    $('#tracktarl').html(targethtml);
                    changeCountryAndPageid(0);
                }
                var selectBox = $("select#tracktarl").data("selectBox-selectBoxIt");
                selectBox.refresh();
            }else if(type==2){
                if(targethtml==''){
                    $('#ratetarl').html('<option>select...</option>');
                    var selectBox = $("select#ratetarl").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    //first remove the selected before.
                    $('li[class=select2-search-choice]').remove();
                    $('#ratefacl').html('');
                    $('#ratecountry').html('<option value="0">select...</option>');
                    var selectBox = $("select#ratecountry").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    $('#ratepageid').html('<option value="0">select...</option>');
                    var selectBox = $("select#ratepageid").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                }else{
                    //refresh factor mutilselect.
                    $('li[class=select2-search-choice]').remove();
                    $('#ratetarl').html('');
                    $('#ratetarl').html(targethtml);
                    var selectBox = $("select#ratetarl").data("selectBox-selectBoxIt");
                    selectBox.refresh();
                    var firstOption = $('#ratetarl').find("option").first();
                    var fval = firstOption.val();
                    changeFactor(fval);
                    showCountryAndPageId(fval,firstOption.attr('country'),firstOption.attr('pageid'),'rate');
                }
            }

        }
    });
}
function changeFactor(targetid){//type 0 tracking 1 abtest
    $("#abtestfacl").select2("val", "");
    $("#ratefacl").select2("val", "");
    $.ajax({
        type: "GET",
        url: "/factor/getFactorByTargetId",
        async:true,
        data:{targetid:targetid},
        success: function (resp) {
            resp = eval("("+resp+")");
            var factors = resp.factorInfo;
            var html='',data=[];
            $.each(factors,function(i,factor){
                html = html + '    <option value="' + factor.id + '">' + factor.name + '</option>'
            });
            $("#abtestfacl").html(html);
            $("#ratefacl").html(html);
        }
    });
}
function showCountryAndPageId(targetid,country,pageid,type){
	var doh = '',countryelem='acountry',pageidelem='apageid';
	if(type=='abtest'){
		doh = $('#abtesttimeunit').val();
	}else if(type=='rate'){
        doh = $('#ratetimeunit').val();
        countryelem='ratecountry';
        pageidelem='ratepageid';
    }else{
		doh = $('#tracktimeunit').val();
		countryelem='tcountry';
		pageidelem='tpageid';
	}
	if(country==1){
		$.ajax({
	        type: "GET",
	        url: "/report/getTargetCountryOrPageid",
	        async:true,
	        data:{targetid:targetid,type:'country',doh:doh},
	        success: function (resp) {
	            resp = eval("("+resp+")");
	            var records = resp.data;
	            var html='<option value="0">select...</option>',data=[];
	            $.each(records,function(i,record){
	                html = html + '    <option value="' + record.country + '">' + record.country + '</option>'
	            });
	            $('#'+countryelem).html(html);
	            var selectBox = $("select#"+countryelem).data("selectBox-selectBoxIt");
                selectBox.refresh();
	        }
	    });
	}else{
		$('#'+countryelem).html('<option value="0">select...</option>');
		var selectBox = $("select#"+countryelem).data("selectBox-selectBoxIt");
        selectBox.refresh();
	}
	if(pageid==1){
		$.ajax({
	        type: "GET",
	        url: "/report/getTargetCountryOrPageid",
	        async:true,
	        data:{targetid:targetid,type:'pageId',doh:doh},
	        success: function (resp) {
	            resp = eval("("+resp+")");
	            var records = resp.data;
	            var html='<option value="0">select...</option>',data=[];
	            $.each(records,function(i,record){
	                html = html + '    <option value="' + record.pageId + '">' + record.pageId + '</option>'
	            });
	            $('#'+pageidelem).html(html);
	            var selectBox = $("select#"+pageidelem).data("selectBox-selectBoxIt");
                selectBox.refresh();
	        }
	    });
	}else{
		$('#'+pageidelem).html('<option value="0">select...</option>');
		var selectBox = $("select#"+pageidelem).data("selectBox-selectBoxIt");
        selectBox.refresh();
	}
}
function aUnitTime(type,uvalue){
    if(type==0){
        $('#abtestbar').replaceWith('<div id="abtestbar" style="height: 600px; width: 50%; float: left;"></div>');
        $('#aoursofdaybar').replaceWith('<div id="aoursofdaybar" style="height: 450px; width: 50%; float: right;"></div>');
        $('#percentbar').replaceWith('<div id="percentbar" style="height: 450px; width: 50%; float: right;"></div>');
    }else if(type==1){
        if(uvalue=='d'){
            $('#trackbar').replaceWith('<div id="trackbar" style="height: 450px; width: 50%; float: left;"></div>');
            $('#toursofdaybar').replaceWith('<div id="toursofdaybar" style="height: 450px; width: 50%; float: right;"></div>');
        }else{
            $('#trackbar').replaceWith('<div id="trackbar" style="height: 450px; width: 50%; float: left;"></div>');
            $('#toursofdaybar').replaceWith('<div id="toursofdaybar" style="height: 450px; width: 50%; float: right;"></div>');
            $('#trackbar').width('100%');
            $('#toursofdaybar').hide();
        }
    }else{
        $('#ratebar').replaceWith('<div id="ratebar" style="height: 600px; width: 49%; float: left;"></div>');
        $('#ratevpbar').replaceWith('<div id="ratevpbar" style="height: 600px; width: 49%;float: left;"></div>');
        $('#blank').replaceWith('<div id="blank" style="height: 300px; width: 49%;float: left;"></div>');
        $('#rateoursofdaybar').replaceWith('<div id="rateoursofdaybar" style="height: 600px; width: 50%; float: right;"></div>');
        $('#rateclickcountbar').replaceWith('<div id="rateclickcountbar" style="height: 600px; width: 50%; float: right;"></div>');
        $('#rateshowcountbar').replaceWith('<div id="rateshowcountbar" style="height: 600px; width: 50%; float: right;"></div>');
    }
}
function changeCountryAndPageid(type){
    var container = '',reporttype = '';
    if(type==0){
        container = 'tracktarl';
        reporttype = 'tracking';
    }else if(type==2){
        container = 'ratetarl';
        reporttype = 'rate';
    }else{
        container = 'abtesttarl';
        reporttype = 'abtest';
    }
    var firstOption = $('#'+container).find("option").first();
    var fval = firstOption.val();
    showCountryAndPageId(fval,firstOption.attr('country'),firstOption.attr('pageid'),reporttype);
}
function changeCondition(type){
    if(type==0){
        //hide condition
        $("#commoncondition").hide();
        $("#ratecondition").hide();
    }else{
        //show condition
        $("#commoncondition").show();
        $("#ratecondition").show();
    }
}
var maxDate,startMaxDate;
function get_time(){
    var dd = new Date();
    dd.setDate(dd.getDate()-1);
    var date=new Date();
    var year="",month="",day="",week="",hour="",minute="",second="";
    year=date.getFullYear();
    month=add_zero(date.getMonth()+1);
    day=add_zero(date.getDate());
    week=date.getDay();
    switch (date.getDay()) {
        case 0:val="Sunday";break;
        case 1:val="Monday";break;
        case 2:val="Tuesday";break;
        case 3:val="Wednesday";break;
        case 4:val="Thursday";break;
        case 5:val="Friday";break;
        case 6:val="Saturday";break;
    }
    hour=add_zero(date.getHours());
    minute=add_zero(date.getMinutes());
    second=add_zero(date.getSeconds());
    maxDate = year+"-"+month+"-"+day;
    startMaxDate = year+"-"+month+"-"+add_zero(dd.getDate());
    $('#nowtimeid').html(" "+year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second+" "+val);
    var timeID=setTimeout(get_time,1000);
}
function add_zero(temp){
    if(temp<10) return "0"+temp;
    else return temp;
}
function ignoreSpaces(string) {
    var temp = "";
    string = '' + string;
    splitstring = string.split(" ");
    for(i = 0; i < splitstring.length; i++)
        temp += splitstring[i];
    return temp;
}
function trim(str){
    return str.replace(/(^\s*)|(\s*$)/g, "");
}
$(function(){
    var selectBoxItArr = ["abtesttimeunit","tracktimeunit","ratetimeunit","abtestprol","abtesttarl","trackprol","tracktarl","rateprol","ratetarl","acountry","apageid","tcountry","tpageid","ratecountry","ratepageid"];
    $("#userinfobtn").click(editInfo);
    $("#abtestaddpro").click(showProForm);
    $("#abteststa").click(abtestgetPaneChart);
    $("#ratesta").click(rategetPaneChart);
    $("#tracksta").click(trackgetPaneChart);
    //$("#abtestreport").slimScroll({ height: '500px'});

    for(var i=0;i<selectBoxItArr.length;i++){
        $("#"+selectBoxItArr[i]).selectBoxIt({
            showEffect: 'fadeIn',
            hideEffect: 'fadeOut'
        });
    }

    $("#abtestfacl").select2({
        placeholder: 'Choose your Factors.',
        allowClear: true
    }).on('select2-open', function()
    {
        // Adding Custom Scrollbar
        $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
    });
    $("#ratefacl").select2({
        placeholder: 'Choose your Factors.',
        allowClear: true
    }).on('select2-open', function()
    {
        // Adding Custom Scrollbar
        $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
    });

    if (proTable == undefined || proTable == "undefined" || proTable == null) {
    }else{
        proTable.fnDestroy();
    }
    proTable = $('#t_project_data').dataTable({
        "bPaginate" : true,
        "bLengthChange" : true,
        "processing": true,
        "bDestroy": true,
        "bRetrieve": true,
        "bProcessing": true,
        bFilter:true,
        aLengthMenu: [
            [5,10, 25, 50, 100, -1], [5,10, 25, 50, 100, "All"]
        ],
        "oLanguage": {
            "sProcessing": "<div style='margin-left:10px;'><img src='/resources/images/loading.gif'></div>"
        },
        "sPaginationType": "full_numbers",
        "bServerSide" : true,
        "sAjaxSource" : "/project/list?rand="+Math.random(),
        "fnServerData" : function(sSource, aoData, fnCallback) {
            $.ajax({
                "type" : "get",
                "url" : sSource,
                "dataType" : "json",
                "data" : {
                    aoData : JSON.stringify(aoData),
                    "type" : 5
                },
                "success" : function(resp) {
                    fnCallback(resp);
                }
            });
        },
        "aoColumns": [
            {"sTitle": "Name" , "bSortable": true,"bVisible": true,"sWidth": "15%",  "mData": function(source, type, val){
                var proname = source.name;
                var html = '<a title="'+proname+'" href="javascript:void(0);" onclick="getTarget('+source.id+',\''+proname+'\',\''+source.active+'\')">'+proname+'</a>';
                if(proname.length>30){
                    html = '<a title="'+proname+'" href="javascript:void(0);" onclick="getTarget('+source.id+',\''+proname+'\',\''+source.active+'\')">'+proname.substring(0,30)+'...</a>';
                }
                return html;
            }},
            {"sTitle": "Description", "bSortable": false,"sWidth": "20%","mData": function(source,type,val){
                var description = source.description;
                var html = '<p title="'+description+'">'+description+'</p>';
                if(description.length>40){
                    html = '<p title="'+description+'" >'+description.substring(0,40)+'...</a>';
                }
                return html;
            }},//, "bVisible": false},
            {"sTitle": "Createtime" ,"bSortable": true,"sWidth": "15%","mData": "createtime"},
            {"sTitle": "Status" ,"bSortable": true,"sWidth": "10%","mData": "active"},
            {"sTitle": "Operation" ,"bSortable": false,"sWidth": "15%","mData": function(source, type, val){
                var html='';
                html+='<a href="javascript:void(0);" class="btn btn-secondary btn-sm btn-icon icon-left" onclick="editPro('+source.id+', \''+source.name+'\', \''+source.description+'\',\''+source.active+'\')">'+
                'Edit'+
                '</a>'+
                '<a href="javascript:void(0);" class="btn btn-danger btn-sm btn-icon icon-left" onclick="deletePro('+source.id+')">'+
                'Delete'+
                '</a>'+
                '<a href="javascript:void(0);" class="btn btn-info btn-sm btn-icon icon-left" onclick="changePro('+source.id+', \''+source.active+'\')">'+
                (source.active=='active'?'inactive':'active')+
                '</a>';
                return html;
            }}
           ]
    }).yadcf([
//            {column_number : 0, filter_type: 'text'},
        {column_number : 2, filter_type: 'text'},
        {column_number : 3}
    ]);;

    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var activeTab = $(e.target).text();
        //when click ChartsReport then load project condition info.
        if(trim(activeTab)=='ChartsReport'){
            $.ajax({
                type: "GET",
                url: "/project/getProject",
                async:true,
                success: function (resp) {
                    resp = eval("("+resp+")");
                    var pros = resp.projectInfo;
                    var prohtml = '';
                    $.each(pros,function(i,pro){
                        prohtml+="<option value='"+pro.id+"'>"+pro.name+"</option>";
                    });
                    if(prohtml!=''){
                        $('#abtestprol').html(prohtml);
                        $('#trackprol').html(prohtml);
                        $('#rateprol').html(prohtml);
                        var abtestSelectBox = $("select#abtestprol").data("selectBox-selectBoxIt");
                        abtestSelectBox.refresh();
                        var trackSelectBox = $("select#trackprol").data("selectBox-selectBoxIt");
                        trackSelectBox.refresh();
                        var rateSelectBox = $("select#rateprol").data("selectBox-selectBoxIt");
                        rateSelectBox.refresh();
                        var firstOption = $('#abtestprol').find("option").first();
                        var fval = firstOption.val();
                        // init abtest and track target
                        changeTarget(0,fval);
                        changeTarget(1,fval);
                        changeTarget(2,fval);
                    }
                }
            });
        }
    });
    function rategetPaneChart(){
        var rv = $('#ratetimeunit').val();
        var startTime = $('#rateStartTime').val();
        var endTime = $('#rateEndTime').val();
        var pid = $('#rateprol').val();
        var tid = $('#ratetarl').val();
        var fids = $("#ratefacl").select2("val");
        var country = $('#ratecountry').val();
        var pageid = $('#ratepageid').val();
        if(rv=='d'){
            aUnitTime(2,'d');

        }else{
            aUnitTime(2,'h');
        }
        if(startTime==''){
            artAlert('Please choose start date');
            return false;
        }
        if(endTime==''){
            artAlert('Please choose end date');
            return false;
        }
        if(isNaN(tid)){
            artAlert('Please select Target,Change Project to change target.');
            return false;
        }
        if(fids==null||fids==''||fids.length==0){
            artAlert('Please select Factor,Change Target to change factor.');
            return false;
        }

        $.ajax({
            type: "GET",
            url: "/report/rateReport",
            async:false,
            data:{dorh:rv,startTime:startTime,endTime:endTime,project:pid,target:tid,fids:''+fids,country:country,pageid:pageid},
            success: function (panedata) {
                var pieElemId = 'ratebar',vppieElemId='ratevpbar',dayRateLineId='rateoursofdaybar',dayClickLineId='rateclickcountbar',dayShowLineId='rateshowcountbar';
                panedata = eval("("+panedata+")");

                getAbtestDailyPane(panedata.regions,startTime,endTime,pieElemId,'count');
                getAbtestDailyPane(panedata.vpregions,startTime,endTime,vppieElemId,'vpcount');
                dayToDayLine(panedata,startTime,endTime,dayRateLineId,rv,'rate');
                dayToDayLine(panedata,startTime,endTime,dayClickLineId,rv,'count');
                dayToDayLine(panedata,startTime,endTime,dayShowLineId,rv,'vpcount');
            }
        });
    }
    function abtestgetPaneChart(){
        var rv = $('#abtesttimeunit').val();
        var startTime = $('#abtestStartTime').val();
        var endTime = $('#abtestEndTime').val();
        var pid = $('#abtestprol').val();
        var tid = $('#abtesttarl').val();
        var fids = $("#abtestfacl").select2("val");
        var country = $('#acountry').val();
        var pageid = $('#apageid').val();
        if(rv=='d'){
            aUnitTime(0,'d');

        }else{
            aUnitTime(0,'h');
        }
        if(startTime==''){
            artAlert('Please choose start date');
            return false;
        }
        if(endTime==''){
            artAlert('Please choose end date');
            return false;
        }
        if(isNaN(tid)){
            artAlert('Please select Target,Change Project to change target.');
            return false;
        }
        if(fids==null||fids==''||fids.length==0){
            artAlert('Please select Factor,Change Target to change factor.');
            return false;
        }

        $.ajax({
            type: "GET",
            url: "/report/abtestReport",
            async:false,
            data:{dorh:rv,startTime:startTime,endTime:endTime,project:pid,target:tid,fids:''+fids,country:country,pageid:pageid},
            success: function (panedata) {
                var pieElemId = 'abtestbar',dayLineId='aoursofdaybar',percentId='percentbar';
                panedata = eval("("+panedata+")");
                if(rv=='d'){
                    getAbtestDailyPane(panedata.regions,startTime,endTime,pieElemId,'abtest');
                    dayToDayLine(panedata,startTime,endTime,dayLineId,rv,'abtest');
                }else{
                    abtestHourlyCountPie(panedata.regions,startTime,endTime,pieElemId);
                    getAbtestHourlyChart(panedata,startTime,endTime,dayLineId);
                }
                abtestPercentBar(panedata,startTime,endTime,percentId);
            }
        });
    }
    function getAbtestHourlyChart(panedata,startTime,endTime,elemId){
        var dataSource = panedata.lineData;
        var series = panedata.series;
        if(series.length==0){
            $("#"+elemId).html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#"+elemId).dxChart({
                dataSource: dataSource,
                commonSeriesSettings: {
                    argumentField: "date"
                },
                series: series,
                argumentAxis: {
                    valueMarginsEnabled: false,
                    discreteAxisDivisionMode: "crossLabels",
                    grid: {
                        visible: true
                    }
                },
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                title: startTime+"--"+endTime,
                legend: {
                    verticalAlignment: "bottom",
                    horizontalAlignment: "center"
                }
            });
        }

    }
    function  getAbtestDailyPane(backData,startTime,endTime,elemId,type){
        var title = "Count Chart At "+startTime+"-"+endTime;
        if(type=='count'){
            title ="Click Count Chart At "+startTime+"-"+endTime;
        }else if(type=='vpcount'){
            title ="Show Count Chart At "+startTime+"-"+endTime;
        }
        var dataSource = backData;
        var xenonPalette = ['#68b828','#7c38bc','#0e62c7','#fcd036','#4fcdfc','#00b19d','#ff6264','#f7aa47'];
        var  timer;
        if(dataSource.length==0){
            $("#"+elemId).html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#"+elemId).dxPieChart({
                dataSource: dataSource,
                series: [{
                    argumentField: "region",
                    valueField: "val",
                    type: "doughnut",
                    label:{
                        visible: true,
                        connector:{
                            visible:true,
                            width: 1
                        }
                    }
                }],
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                palette: xenonPalette,
                title: title,
                onPointClick: function(e) {
                    var point = e.target;
                    point.isVisible() ? point.hide() : point.show();
                }
            });
        }

    }
    function dayToDayLine(panedata,startTime,endTime,elemId,dorh,type){
        var height = type=='abtest'?'450px':'600px';
        var title = "Daily Chart Between "+startTime+" And "+endTime;
        if(type=='rate'){
            if(dorh=='d'){
                title = "Daily Rate Chart Between "+startTime+" And "+endTime;
            }else{
                title = "Hourly Rate Chart Between "+startTime+" And "+endTime;
            }
        }else if(type=='count'){
            if(dorh=='d'){
                title = "Daily Click Chart Between "+startTime+" And "+endTime;
            }else{
                title = "Hourly Click Chart Between "+startTime+" And "+endTime;
            }
        }else if(type=='vpcount'){
            if(dorh=='d') {
                title = "Daily Show Chart Between " + startTime + " And " + endTime;
            }else{
                title = "Hourly Show Chart Between " + startTime + " And " + endTime;
            }
        }
        $('#'+elemId).replaceWith('<div id="'+elemId+'" style="height: '+height+'; width: 50%; float: right;"></div>');
        var dataSource = panedata.lineData;
        if(type=='count'){
            dataSource = panedata.countmaps;
        }else if(type=='vpcount'){
            dataSource = panedata.vpcountmaps
        }
        var series = panedata.series;
        var  timer;
        if(series.length==0){
            $("#"+elemId).html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#"+elemId).dxChart({
                dataSource: dataSource,
                commonSeriesSettings: {
                    argumentField: "date"
                },
                series: series,
                argumentAxis: {
                    valueMarginsEnabled: false,
                    discreteAxisDivisionMode: "crossLabels",
                    grid: {
                        visible: true
                    }
                },
                tooltip:{
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                title: title,
                legend: {
                    verticalAlignment: "bottom",
                    horizontalAlignment: "center"
                },
                pointClick: function(point) {
                    point.showTooltip();
                    clearTimeout(timer);
                    timer = setTimeout(function() { point.hideTooltip(); }, 2000);
                }
            });
        }
    }
    function abtestPercentBar(panedata,startTime,endTime,elemId){
        $('#'+elemId).replaceWith('<div id="'+elemId+'" style="height: 450px; width: 50%; float: right;"></div>');
        var dataSource = panedata.percentData;
        var series = panedata.series;
        if(series.length==0){
            $("#"+elemId).html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#"+elemId).dxChart({
                dataSource: dataSource,
                commonSeriesSettings: {
                    argumentField: "date"
                },
                argumentAxis: {
                    valueMarginsEnabled: false,
                    discreteAxisDivisionMode: "crossLabels",
                    grid: {
                        visible: true
                    }
                },
                series: series,
                legend: {
                    verticalAlignment: "bottom",
                    horizontalAlignment: "center",
                    itemTextPosition: "right"
                },
                title: "Percent Chart At "+startTime+' -- '+endTime,
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument + " - " + arg.valueText
                        };
                    }
                },
                pointClick:function(point){
                    console.info(point);
                }
            });
        }
    }
    function abtestHourlyCountPie(dataSource,startTime,endTime,elemId){
        var xenonPalette = ['#68b828','#7c38bc','#0e62c7','#fcd036','#4fcdfc','#00b19d','#ff6264','#f7aa47'];
        var  timer;
        if(dataSource.length==0){
            $("#"+elemId).html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#"+elemId).dxPieChart({
                dataSource: dataSource,
                title: "Count Chart At "+startTime+"-"+endTime,
                tooltip: {
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                pointClick: function(point) {
                    //var fname = point.argument;
                },
                legend: {
                    visible: true
                },
                series: [{
                    type: "doughnut",
                    argumentField: "region",
                    label:{
                        visible: true,
                        connector:{
                            visible:true,
                            width: 1
                        }
                    }
                }],
                palette: xenonPalette
            });
        }
    }
    function trackgetPaneChart(){
        var rv = $('#tracktimeunit').val();
        if(rv=='d'){
            aUnitTime(1,'d');
        }else{
            aUnitTime(1,'h');
        }
        var startTime = $('#trackStartTime').val();
        var endTime = $('#trackEndTime').val();
        var pid = $('#trackprol').val();
        var tid = $('#tracktarl').val();
        var country = $('#tcountry').val();
        var pageid = $('#tpageid').val();
        if(startTime==''){
            artAlert('Please choose start date');
            return false;
        }
        if(endTime==''){
            artAlert('Please choose end date');
            return false;
        }
        if(isNaN(tid)){
            artAlert('Please select Target,Change Project to change target.');
            return false;
        }
        $.ajax({
            type: "GET",
            url: "/report/trackReport",
            async:false,
            data:{dorh:rv,startTime:startTime,endTime:endTime,project:pid,target:tid,country:country,pageid:pageid},
            success: function (panedata) {
                panedata = eval("("+panedata+")");
                getTrackDailyPane(rv,panedata,startTime,endTime,pid,tid,country,pageid);
            }
        });
    }
    function getTrackDailyPane(type,panedata,startTime,endTime,pid,tid,country,pageid){
        var dataSource = panedata.lineData;
        var series = panedata.series;
        var  timer;
        if(series.length==0){
            $("#trackbar").html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#trackbar").dxChart({
                dataSource: dataSource,
                commonSeriesSettings: {
                    argumentField: "date"
                },
                series: series,
                argumentAxis: {
                    valueMarginsEnabled: false,
                    discreteAxisDivisionMode: "crossLabels",
                    grid: {
                        visible: true
                    }
                },
                tooltip:{
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                title: startTime+"--"+endTime,
                legend: {
                    verticalAlignment: "bottom",
                    horizontalAlignment: "center"
                },
                pointClick: function(point) {
                    point.showTooltip();
                    clearTimeout(timer);
                    timer = setTimeout(function() { point.hideTooltip(); }, 2000);
                    if(type=='d'){
                        var selectDate = point.argument;
                        var tname = point.series.name;
                        $.ajax({
                            type: "GET",
                            url: "/report/trackReport",
                            async:false,
                            data:{dorh:'allDay',project:pid,target:tid,selectDate:selectDate,tname:tname.split('(')[0],country:country,pageid:pageid},
                            success: function (panedata) {
                                panedata = eval("("+panedata+")");
                                tdayToDayLine(panedata,tname.split('(')[0],selectDate);
                            }
                        });
                    }
                }
            });
        }
    }
    function tdayToDayLine(panedata,tname,selectDate){
        $('#toursofdaybar').replaceWith('<div id="toursofdaybar" style="height: 450px; width: 50%; float: right;"></div>');
        var dataSource = panedata.lineData;
        var series = panedata.series;
        var  timer;
        if(series.length==0){
            $("#toursofdaybar").html("<div style='color:red;font-size:140%;margin-top:50px;'>No Data For Report.</div>");
        }else{
            $("#toursofdaybar").dxChart({
                dataSource: dataSource,
                commonSeriesSettings: {
                    argumentField: "date"
                },
                series: series,
                argumentAxis: {
                    valueMarginsEnabled: false,
                    discreteAxisDivisionMode: "crossLabels",
                    grid: {
                        visible: true
                    }
                },
                tooltip:{
                    enabled: true,
                    customizeTooltip: function (arg) {
                        return {
                            text: arg.originalArgument+':'+arg.originalValue
                        };
                    }
                },
                title: tname+" Tracking Chart At "+selectDate,
                legend: {
                    verticalAlignment: "bottom",
                    horizontalAlignment: "center"
                },
                pointClick: function(point) {
                    point.showTooltip();
                    clearTimeout(timer);
                    timer = setTimeout(function() { point.hideTooltip(); }, 2000);
                }
            });
        }
    }
    function editInfo(){
        var oldusername = $('#username').val();
        var oldpass = $('#password').val();
        if(userinfoWin==null){
            userinfoWin = dialog({
                title: 'User Information',
                content: userInfo,
                width:330,
                okValue: 'Confirm',
                ok: function () {
                    var userid = $('#userid').val();
                    var username = $('#username').val();
                    var pass = $('#password').val();
                    var cpass = $('#cpassword').val();
                    var oldpass = $('#oldpassword').val();
                    $('#name-error').html('');
                    $('#pass-error').html('');
                    $('#cpass-error').html('');
                    $('#oldpass-error').html('');
                    var flag = true;
                    if(trim(username)==''){
                        $('#name-error').html('<font color="#ff0000">Username is empty.</font>');
                        flag = false;
                    }
                    if(trim(oldpass)==''){
                        $('#oldpass-error').html('<font color="#ff0000">Old Password is empty.</font>');
                        flag = false;
                    }
                    if(trim(pass)==''){
                        $('#pass-error').html('<font color="#ff0000">Password is empty.</font>');
                        flag = false;
                    }
                    if(trim(cpass)==''){
                        $('#cpass-error').html('<font color="#ff0000">Confirm Password is empty.</font>');
                        flag = false;
                    }
                    if(trim(pass)!=trim(cpass)){
                        $('#cpass-error').html('<font color="#ff0000">Confirm Password must the same as the Password.</font>');
                        flag = false;
                    }
                    if(!flag){
                        return flag;
                    }
                    $.ajax({
                        type: "GET",
                        url: "/user/doRegist",
                        async:false,
                        data:{userid:userid,username:username,oldpass:oldpass,password:pass,isnew:1},
                        success: function (resp) {
                            resp = eval("(" + resp + ")");
                            if(resp.success){
                                $('#username').val(resp.username);
                                $('#oldpassword').val('');
                                $('#password').val('');
                                $('#cpassword').val('');
                            }else{
                                $('#username').val(oldusername);
                            }
                            $('#name-error').html('');
                            $('#pass-error').html('');
                            $('#cpass-error').html('');
                            $('#oldpass-error').html('');
                            userinfoWin.close();userinfoWin=null;
                            artAlert(resp.msg);
                        }
                    });
                },
                cancelValue: 'Cancel',
                cancel: function () {
                    userinfoWin.close();
                    userinfoWin=null;
                    $('#username').val(oldusername);
                    $('#oldpassword').val('');
                    $('#password').val('');
                    $('#cpassword').val('');
                    $('#name-error').html('');
                    $('#pass-error').html('');
                    $('#cpass-error').html('');
                    $('#oldpass-error').html('');
                }
            });
        }
        userinfoWin.showModal();
    }
    get_time();
});
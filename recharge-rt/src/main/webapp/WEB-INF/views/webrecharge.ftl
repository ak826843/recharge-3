


<div align="center" >
<form id="myform" action="/recharge/app/webrecharge" method="post">

<table >

<tr>
	<td>面值（元）：</td>
	<td>
		<select id="faceValue" name="faceValue">
			<option value="">--选择面值--</option>
			<option value="100">100</option>
			<option value="50">50</option>
			<option value="30">30</option>
		</select>
	</td>
</tr>
<tr>
	<td>密钥：</td>
	<td>
		<input type="text" name="secretKey" id="secretKey" />
	</td>
</tr>
<tr >
	<td colspan="2">手机号：<div>（多个手机号之间以英文半角,分隔。如：18611111111,18622222222）</div></td>
	
	</tr>
	<tr >
	<td  colspan="2">
		<textarea name="mobiles"  id="mobiles" style="overflow:hidden; width:406px;" rows="2"
		  onkeyup="resize(this)"></textarea>
	</td>
</tr>

</table>

<div>
	<input type="button" value="充值" onclick="check()" />
	<input type="reset" value="清空" />
</div>

</form>

</div> 


<script type="text/javascript" language="JavaScript">
function trim(str){ 
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

function check(){
	var faceValue=document.getElementById("faceValue").value;
	var secretKey=document.getElementById("secretKey").value;
	var mobiles=document.getElementById("mobiles").value;
	if(faceValue=="" || trim(faceValue).length==0){
		alert("请选择'面值'");
		document.getElementById("faceValue").focus();
		return false;
	}
	if(secretKey=="" || trim(secretKey).length==0){
		alert("请输入'密钥'");
		document.getElementById("secretKey").focus();
		return false;
	}
	if(mobiles=="" || trim(mobiles).length==0){
		alert("请输入'手机号'");
		document.getElementById("mobiles").focus();
		return false;
	}
	
	document.getElementById("myform").submit();
}

function resize(textArea){
	var expectedRows=2;
	var content=textArea.value;
	expectedRows += Math.ceil( content.length / 60 );
	if(expectedRows != textArea.rows){
		textArea.rows=expectedRows;
	}
}
</script>



function GridRowButtonGroup(){
	this.buttonArray = new Array();
	return this;
}

GridRowButtonGroup.prototype.addButton=function(button){
	this.buttonArray.push(button);
	return this;
};
GridRowButtonGroup.prototype.draw=function(){
	var str ="<div >";
	for(var i=0;i<this.buttonArray.length;i++){
		str = str+this.buttonArray[i].draw();
    }
	return str+"</div>";
};
function GridRowButton(text,id,gridId,rowId,onclick,icons){
	this.text = text;
	this.id=id;
	this.gridId=gridId;
	this.rowId= rowId;
	this.onclick=onclick;
	this.icons = icons;
	return this;
}
GridRowButton.prototype.draw=function(){
	var str = "<button  class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary' role='button' type='button' id='"+this.id+"_"+this.rowId+"'";
	str = str+" title='" +this.text+"'";
	if(this.onclick){
		str = str+" onclick='"+this.onclick+"(\""+this.gridId+"\",\""+this.rowId+"\")'";
	}
	str = str+">";
	if(this.icons){
		str = str+"<span class='ui-button-icon-primary ui-icon "+this.icons+"'></span>";
	}
	str = str+"<span class='ui-button-text'>"+this.text+"</span></button>";
	return str;
};
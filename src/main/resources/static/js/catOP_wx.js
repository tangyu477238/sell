var busType = function () {
	this.config = {};
	
	this.click = function () {
		var left = $('form [name=left]').val(),
			right = $('form [name=right]').val(),
			rows = $('form [name=rows]').val();
		rows = parseInt(rows);
		left = parseInt(left);
		right = parseInt(right);
		if (!(rows> 0)) return;
		$('form [name=arrangement]').val('左' + left + '排，右' + right + '排，' + rows + '列');
		//this.initTable(left, right, rows);
	}
	this.initData = function (d, imgPath, saled, maxSelected, maxSelectedMsg) {
		var rows = d.rows,
			left = d.left,
			right = d.right;
		this.imgPath = imgPath;
		this.config.left = left;
		this.config.right = right;
		this.config.rows = rows;
		this.saled = saled.split(',');
		this.maxSelected = maxSelected;
		this.maxSelectedMsg = maxSelectedMsg;
		this.imgs = {ok:imgPath + 'enb.png', no: imgPath + 'dis.png', selected: imgPath + 'wx_slted.png', love: imgPath + 'love.png'};
		for(var i = 0;i <this.saled.length; i++) {
			this.saled[i] = $.trim(this.saled[i]); 
		}
		//$('form [name=arrangement]').val('左' + left + '排，右' + right + '排，' + rows + '列');
		var t = '<table style="">';
		for(var i = rows;i >= 1; i--) {
			t += this.genColSeat(left, right, i, d[i], rows);
		}
		t += '</table>';
		$('#234l32j4').html(t);
		//$('form [name=data]').val($.toJSON(this.getData()));
	}
	this.canSale = function (siteName) {
		for(var i = 0;i <this.saled.length; i++) {
			if (this.saled[i] == siteName ) return false;
		}
		return true;
	}
	this.genColSeat = function(left, rigth, rowIndex, override, rows) {
		if (!$.isArray(override)) override = [];
		//675px;height: 850px 
		var w = Math.round(190 / (left + rigth +1) - 6);
		var h = Math.round(400 / rows - 6);
		var t = '<tr rowIndex="' + rowIndex + '">';
		var style = 'width: ' +w+ 'px;height: '+h+'px;line-height: '+h+'px; text-align:center;font-size: 7px;border:0px solid #000;cursor: pointer;';
		var style2 = style + ';border:0;';
		
		for(var i = 1;i <=left + rigth +1; i++) {
			//colIndex\":\"1\",\"isSeat\":\"0\",\"name\":\"\u8fc7\u9053\"
			
			var styleX = null; 
			var isSeat = null;
			var seatName = null;
			var cfg = override[i-1] || {};
			var saleName = '';
			if (i == left +1) { //过道
				styleX = style2,
				seatName = '过道';
				isSeat = 0;
			} else if (rowIndex == 0 && i <= left + rigth) { // 司机台
				styleX = style2,
				seatName = '过道';
				isSeat = 0;
			} else {
				seatName = rowIndex +'排' +　i + '座';
				isSeat = 1;
			}
			
			seatName = cfg.name || seatName;
			isSeat = cfg.seatType || isSeat;
			if (seatName == '过道') seatName ='';
			if (isSeat == 1) { // 座位
				if (this.canSale(seatName)) {
					styleX = style + ';background:url(' + this.imgs.ok + ');background-size: ' + w + 'px ' + h + 'px;';
				} else {
					isSeat = 0;
					styleX = style + ';background:url(' + this.imgs.no + ');background-size: ' + w + 'px ' + h + 'px;';
				}
				
			} else if (isSeat == 2) { //爱心
				styleX = style + ';background:url(' + this.imgs.love + ');background-size: ' + w + 'px ' + h + 'px;';
				saleName = '';
			} else { // 过道
				styleX = style2;
			}
			var img = '<src="wdith: 100;height: 100%;" src="' +this.imgPath + + '">"'
			t += '<td><div colIndex=' + i + ' class="seat" style="' + styleX + '" seat="' +isSeat+ '" seatName="' + seatName+ '">' + saleName + '</div></td>';
		}
		t += '</tr>';
		return t;
	}
	var me = this;
	$('#234l32j4').on('click', function (e) {
		var el = $(e.target);
		if (el.attr('class') != 'seat') return;
		var isSeat = el.attr('seat');
		if (isSeat == 0 || isSeat== 2) return; //不是座位
		var scted = el.attr('scted');
		if (scted == 1) { 
			el.attr('scted', 0);
			el.css('background-image', 'url("' + (isSeat == 1 ? me.imgs.ok: me.imgs.love) + '")');
			el.html('');
		} else {
			var selectedCount = $('.seat[scted=1]').length;
			if (selectedCount >= me.maxSelected) { //超出范围.
				alert(me.maxSelectedMsg);
				return; 
			}
			//---------------------//
			el.attr('scted', 1);
			el.css('background-image', 'url("' + me.imgs.selected + '")');
			//el.html('已选中')
		}
		
		// set msg.
		var selected = $('.seat[scted=1]');
		var seatNames = [];
		selected.each(function () {
			var seatEl = $(this);
			seatNames.push(seatEl.attr('seatName'));
		})
		$('#seat').val(seatNames.join(','));
		$('#checked').html(seatNames.join(','));
		$('#num').val(seatNames.length);
	});
};
window.by = new busType();
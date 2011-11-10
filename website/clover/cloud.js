function showDeepPackageCloud(show) {
	if (show) {
		document.getElementById('deepPackageCloud').style.display = 'block';
		document.getElementById('shallowPackageCloud').style.display = 'none';
	} else {
		document.getElementById('shallowPackageCloud').style.display = 'block';
		document.getElementById('deepPackageCloud').style.display = 'none';
	}
}

function updateCloudDepthCheckbox() {
	if (document.getElementById('cloudDepthCheckbox')) {
		document.getElementById('cloudDepthCheckbox').checked =
			(document.getElementById('deepPackageCloud') == undefined
			|| document.getElementById('shallowPackageCloud').style.display == 'none');
	}
}
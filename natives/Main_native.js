function _CHEERPJ_COMPRESS(ZN4Main9newJSPathEVEN4java4lang6Object)() {
	return {points:[],bbox:null};
}
function _CHEERPJ_COMPRESS(ZN4Main8addPointEN4java4lang6ObjectFFEV)(path, x1, x2) {
	 path.points.push([x1,x2]);
}
function _CHEERPJ_COMPRESS(ZN4Main7setBBoxEN4java4lang6ObjectFFFFEV)(path, x1, x2, x3, x4) {
	path.bbox = [x1,x2,x3,x4];
}
function _CHEERPJ_COMPRESS(ZN4Main8setErrorEN4java4lang6ObjectN4java4lang6StringEV)(path, err) {
	path.errors = err;
}


export default class Sound {
  constructor(id) {
    this.$ = document.getElementById(id)
  }

  play() {
    // 対象となるID名
    var id = 'sound-file' ;

    // 初回以外だったら音声ファイルを巻き戻す
    if( typeof( this.$.currentTime ) != 'undefined' )
    {
      this.$.currentTime = 0;
    }

    // [ID:sound-file]の音声ファイルを再生[play()]する
    this.$.play() ;
  }
}

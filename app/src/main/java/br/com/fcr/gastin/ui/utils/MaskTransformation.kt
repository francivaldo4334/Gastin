package br.com.fcr.gastin.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MaskTransformation() : VisualTransformation {
    fun maskFilter(text: AnnotatedString): TransformedText {
        var out = ""
        text.forEach {out += it}
        out = Regex("[^0-9]").replace(out,"")
        while (out.length>0 && out[0] == '0')
            out = out.substring(1)
        var FirsMask = "##,###"
        var LastMask = ".##"
        var n0 = out.reversed();
        out = ""
        for(i in 0..n0.length-1){
            if(i >= FirsMask.length-1) break
            if(FirsMask[i] != '#') out += FirsMask[i]
            out += n0[i];
        }
        var run = true;
        var count = 0;
        if(n0.length >= FirsMask.length)
            while (run){
                for (i in 0..LastMask.length-1){
                    var index = FirsMask.length+count-1;
                    if(index > n0.length-1) {
                        run = false;
                        break;
                    }
                    if(LastMask[i]!='#') out += LastMask[i]
                    out += n0[index]
                    count ++;
                }
            }
        else{
            for (i in n0.length .. FirsMask.length-3){
                if (FirsMask[i]!='#') out += FirsMask[i]
                else out += '0';
            }
        }
        out = out.reversed();
        while (out.length>4 && out[0] == '0')
            out = out.substring(1)
        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return out.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text)
    }
}
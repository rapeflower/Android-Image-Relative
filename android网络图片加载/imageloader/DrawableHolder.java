/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.j1.healthcare.patient.utils.imageloader;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.j1.healthcare.patient.R;
import com.j1.healthcare.patient.utils.ImageCommonUtils;

/***********
 *
 * @Author rape flower
 * @Date 2016-09-19 16:40
 * @Describe 配置项目中图片加载：默认图片和加载失败的图片
 *
 */
public class DrawableHolder {

  public static Drawable mPlaceholderDrawable;
  public static Drawable mErrorDrawable;

  private DrawableHolder() {
  }

  public static void init(final Context context) {
    if (mPlaceholderDrawable == null) {
        mPlaceholderDrawable = context.getResources().getDrawable(R.drawable.j1_default_200_200);
    }
    if (mErrorDrawable == null) {
        mErrorDrawable = context.getResources().getDrawable(R.drawable.j1_default_200_200);
    }
  }

  /**
   * 根据资源ID获取Drawable
   * @param context
   * @param drawableId
   * @return
     */
  public static Drawable getDrawable(Context context, int drawableId) {
    Drawable drawable = null;
    if (drawableId >= 0) {
       drawable = context.getResources().getDrawable(drawableId);
    }
    return drawable;
  }
}

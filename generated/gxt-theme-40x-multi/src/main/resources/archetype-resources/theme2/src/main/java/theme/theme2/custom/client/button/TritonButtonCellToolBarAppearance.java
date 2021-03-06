#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.theme.theme2.custom.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.sencha.gxt.themebuilder.template.client.base.button.Css3ButtonCellAppearance.Css3ButtonResources;

public class TritonButtonCellToolBarAppearance<M> extends TritonButtonCellAppearance<M> {

  public interface TritonButtonCellResources extends Css3ButtonResources {

    @Override
    @Source({"${packageInPathFormat}/theme/theme2/client/base/button/Css3ButtonCell.gss",
            "${packageInPathFormat}/theme/theme2/client/base/button/Css3ButtonCellToolBar.gss", "TritonButtonCellToolBar.gss"})
    TritonButtonCellStyle style();
  }

  public interface TritonButtonCellStyle extends TritonButtonStyle {
  }

  public TritonButtonCellToolBarAppearance() {
    super(GWT.<TritonButtonCellResources> create(TritonButtonCellResources.class));
  }
}

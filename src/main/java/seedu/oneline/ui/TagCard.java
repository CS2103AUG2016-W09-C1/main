package seedu.oneline.ui;

import java.util.Map.Entry;

//@@author A0142605N
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.oneline.model.tag.Tag;
import seedu.oneline.model.tag.TagColor;

//@@author A0142605N 
/* 
 * UI component for card that displays tagname and tag category 
 */
public class TagCard extends UiPart{

    private static final String FXML = "TagListCard.fxml";

    @FXML
    private HBox tagCardPane;
    @FXML
    private Label name;    
    
    private Tag tag;
    private TagColor color; 
    
    public TagCard() {

    }

    public static TagCard load(Tag tag, TagColor color){
        TagCard card = new TagCard();
        card.tag = tag;
        card.color = color;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        if (tag == Tag.getDefault()) {
            name.setText("#uncategorised");
        } else {
            name.setText("#" + tag.getTagName());
        }
        tagCardPane.setStyle("-fx-background-color: " + color.toLighterHTMLColor());
    }

    public HBox getLayout() {
        return tagCardPane;
    }

    @Override
    public void setNode(Node node) {
        tagCardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}

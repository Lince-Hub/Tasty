package lt.linas_puplauskas.tasty.fxControllers.component.order;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lt.linas_puplauskas.model.review.Review;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewHandler {

    private final TextField titleField;
    private final Spinner<Integer> ratingSpinner;
    private final TextField createdAtField;
    private final TextArea commentArea;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReviewHandler(TextField titleField,
                         Spinner<Integer> ratingSpinner,
                         TextField createdAtField,
                         TextArea commentArea) {
        this.titleField = titleField;
        this.ratingSpinner = ratingSpinner;
        this.createdAtField = createdAtField;
        this.commentArea = commentArea;

        // Configure spinner
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 3));
    }

    public void fill(Review review) {
        if (review == null) {
            clear();
            return;
        }

        titleField.setText(review.getTitle());
        ratingSpinner.getValueFactory().setValue(review.getRating());

        if (review.getCreatedAt() != null) {
            createdAtField.setText(review.getCreatedAt().format(DATE_FORMATTER));
        } else {
            createdAtField.clear();
        }

        commentArea.setText(review.getComment());
    }

    public void update(Review review) {
        if (review == null) return;

        review.setTitle(titleField.getText());
        review.setRating(ratingSpinner.getValue());
        review.setComment(commentArea.getText());

        if (review.getCreatedAt() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }
    }

    public void clear() {
        titleField.clear();
        ratingSpinner.getValueFactory().setValue(3);
        createdAtField.clear();
        commentArea.clear();
    }

    public Review createNewReview() {
        Review review = new Review();
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(3);
        return review;
    }
}
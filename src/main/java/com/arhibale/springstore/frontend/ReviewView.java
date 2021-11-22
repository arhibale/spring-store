package com.arhibale.springstore.frontend;

import com.arhibale.springstore.entity.ProductEntity;
import com.arhibale.springstore.entity.ReviewEntity;
import com.arhibale.springstore.service.ProductService;
import com.arhibale.springstore.service.ReviewService;
import com.arhibale.springstore.util.PersonUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Route("review")
@PageTitle("Отзывы")
public class ReviewView extends AbstractView implements HasUrlParameter<String> {

    private static final String ADMIN_ROLE = "admin";
    private static final String MANAGER_ROLE = "manager";
    private static final String QUERY_PARAM_PRODUCT_ID = "product";

    private final ReviewService reviewService;
    private final ProductService productService;

    public ReviewView(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        var parametersMap = queryParameters.getParameters();
        var productId = parametersMap.get(QUERY_PARAM_PRODUCT_ID);

        if (CollectionUtils.isNotEmpty(productId)) {
            for (String id : productId) {
                var product = productService.getProductById(UUID.fromString(id));

                add(new H1("Отзывы продукта " + product.getName()), openReview(product), writeReview(product));
            }
            return;
        }

        add(new H1("Отзывов нет!"));
    }

    private VerticalLayout openReview(ProductEntity product) {
        var review = reviewService.getReviewByProductId(product);
        var verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);

        if (CollectionUtils.isNotEmpty(review)) {
            for (ReviewEntity entity : review) {
                if (entity.isModerated() || PersonUtil.isRole(MANAGER_ROLE) || PersonUtil.isRole(ADMIN_ROLE)) {
                    var textArea = new TextArea();
                    textArea.setWidth(500f, Unit.PIXELS);
                    textArea.setLabel(entity.getPersonId().getLogin() + " " + entity.getScore() + "★");
                    textArea.setValue(entity.getReview());
                    verticalLayout.add(textArea, initIsModeratedButton(entity));
                } else {
                    verticalLayout.add(new H3("Отзыв не проверен!"));
                }
            }
        } else {
            verticalLayout.add(new H1("Пока что нет отзывов!"));
        }

        return verticalLayout;
    }

    private HorizontalLayout initIsModeratedButton(ReviewEntity entity) {
        var horizontalLayout = new HorizontalLayout();
        if (PersonUtil.isRole(MANAGER_ROLE) || PersonUtil.isRole(ADMIN_ROLE)) {
            horizontalLayout.add(
                    new Button("True", buttonClickEvent -> {
                entity.setModerated(true);
                reviewService.save(entity);
                Notification.show("Отзыв проверен!");
            }),     new Button("False", buttonClickEvent -> {
                entity.setModerated(false);
                reviewService.save(entity);
                Notification.show("Отзыв проверен!");
            }));
        }
        return horizontalLayout;
    }

    private VerticalLayout writeReview(ProductEntity product) {
        var verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);

        var textReview = new TextArea();
        int charLimit = 255;
        textReview.setWidth(500f, Unit.PIXELS);
        textReview.setMaxLength(charLimit);
        textReview.setValue("Все очень нравится!");
        textReview.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimit));

        verticalLayout.add(new H2("Напишите свой отзыв о продукте!"), textReview);

        var score = new AtomicInteger();
        var horizontalLayout = new HorizontalLayout();

        for (int i = 1; i <= 5; i++) {
            var buttonScore = new Button(String.valueOf(i));
            buttonScore.setWidth(90f, Unit.PIXELS);
            buttonScore.addClickListener(buttonClickEvent -> {
                score.set(Integer.parseInt(buttonScore.getText()));
                textReview.setLabel(Objects.requireNonNull(PersonUtil.getCurrentPerson()).getLogin() + " " + score + "★");
            });
            horizontalLayout.add(buttonScore);
        }

        var sendReviewButton = new Button("Отправить", buttonClickEvent -> {
            var review = new ReviewEntity()
                    .setProductId(product)
                    .setPersonId(PersonUtil.getCurrentPerson())
                    .setReview(textReview.getValue())
                    .setScore(score.get());
            reviewService.save(review);
            Notification.show("Спасибо за отзыв!");
            UI.getCurrent().getPage().reload();
        });

        verticalLayout.add(horizontalLayout, sendReviewButton);
        return verticalLayout;
    }
}
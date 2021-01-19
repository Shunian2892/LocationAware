package com.example.location_aware.ui.launcher;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.location_aware.R;
import com.example.location_aware.ui.LoginScreen;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LogInUITest {

    @Rule
    public ActivityTestRule<LoginScreen> mActivityTestRule = new ActivityTestRule<LoginScreen>(LoginScreen.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_BACKGROUND_LOCATION");

    @Test
    public void logInUITest() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_sign_in), withText("Inloggen bestaande gebruiker"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email_address_sign_in),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailSignIn),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("nseen1305@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password_sign_in),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailSignIn),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("bbbbbb"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.sign_in_user_button), withText("Log in"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.password_sign_in), withText("bbbbbb"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailSignIn),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.password_sign_in), withText("bbbbbb"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailSignIn),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("aaaaaa"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.password_sign_in), withText("aaaaaa"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailSignIn),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.sign_in_user_button), withText("Log in"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton3.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

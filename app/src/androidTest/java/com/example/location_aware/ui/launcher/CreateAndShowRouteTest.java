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
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateAndShowRouteTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION",
                    "android.permission.ACCESS_COARSE_LOCATION",
                    "android.permission.ACCESS_BACKGROUND_LOCATION");

    @Test
    public void createAndShowRouteTest() {

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_list), withContentDescription("Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.menu_makeRoute), withContentDescription("Instellingen"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.create_own_route), withText("Creëer eigen route"),
                        childAtPosition(
                                allOf(withId(R.id.settings_buttons_fragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.setName),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.setlocation),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("breda"), closeSoftKeyboard());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.addButton), withText("Toevoegen"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.setlocation), withText("breda"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText5.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.setlocation), withText("breda"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("breda station"));

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.setlocation), withText("breda station"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(closeSoftKeyboard());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.addButton), withText("Toevoegen"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                3),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.createRouteButton), withText("Creëer route"),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                6),
                        isDisplayed()));
        materialButton6.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.own_route_backButton),
                        childAtPosition(
                                allOf(withId(R.id.ownRouteFragment),
                                        childAtPosition(
                                                withId(R.id.settings_fragment_container),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction bottomNavigationItemView5 = onView(
                allOf(withId(R.id.menu_list), withContentDescription("Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView5.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.route_rv),
                        childAtPosition(
                                withId(R.id.route_rv_fragment),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction bottomNavigationItemView6 = onView(
                allOf(withId(R.id.menu_map), withContentDescription("Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                0),
                        isDisplayed()));
        bottomNavigationItemView6.perform(click());
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

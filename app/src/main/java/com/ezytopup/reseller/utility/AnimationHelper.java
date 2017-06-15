package com.ezytopup.reseller.utility;

import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ezytopup.reseller.R;

/*
 * Android Helpers
 *
 * Copyright (c) 2017 Dani Mahardhika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class AnimationHelper {

    public static Animator show(@NonNull View view) {
        return new Animator(view, Type.SHOW);
    }

    public static Animator hide(@NonNull View view) {
        return new Animator(view, Type.HIDE);
    }

    public static Animator fade(@NonNull View view) {
        return new Animator(view, Type.FADE);
    }

    public static ViewAnimator slideDownIn(@NonNull View view) {
        return new ViewAnimator(view, Type.SLIDE_DOWN_IN);
    }

    public static ViewAnimator slideDownOut(@NonNull View view) {
        return new ViewAnimator(view, Type.SLIDE_DOWN_OUT);
    }

    public static ViewAnimator slideUpIn(@NonNull View view) {
        return new ViewAnimator(view, Type.SLIDE_UP_IN);
    }

    public static ViewAnimator slideUpOut(@NonNull View view) {
        return new ViewAnimator(view, Type.SLIDE_UP_OUT);
    }

    public static class Animator {

        private final View view;
        private final Type type;
        private int duration;
        private TimeInterpolator interpolator;
        private Callback callback;

        private Animator(@NonNull View view, Type type) {
            this.view = view;
            this.type = type;
            this.duration = 200;
            this.interpolator = new DecelerateInterpolator();
        }

        public Animator duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Animator interpolator(@NonNull TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }


        public Animator callback(@NonNull Callback callback) {
            this.callback = callback;
            return this;
        }

        public void start() {
            switch (type) {
                case SHOW:
                    animateShow(this);
                    break;
                case HIDE:
                    animateHide(this);
                    break;
                case FADE:
                    animateFade(this);
                    break;
                default:
                    animateFade(this);
                    break;
            }
        }
    }

    private static void animateShow(final Animator animator) {
        animator.view.clearAnimation();
        animator.view.setScaleX(0f);
        animator.view.setScaleY(0f);
        animator.view.setAlpha(0f);
        animator.view.setVisibility(View.VISIBLE);

        animator.view.animate()
                .setDuration(animator.duration)
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setInterpolator(animator.interpolator)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        super.onAnimationStart(animation);
                        if (animator.callback != null) animator.callback.onAnimationStart();
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        super.onAnimationEnd(animation);
                        if (animator.callback != null) animator.callback.onAnimationEnd();;
                    }
                });
    }

    private static void animateHide(final Animator animator) {
        animator.view.clearAnimation();
        animator.view.setScaleX(1f);
        animator.view.setScaleY(1f);
        animator.view.setAlpha(1f);
        animator.view.setVisibility(View.VISIBLE);

        animator.view.animate()
                .setDuration(animator.duration)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setInterpolator(animator.interpolator)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        super.onAnimationStart(animation);
                        if (animator.callback != null) animator.callback.onAnimationStart();
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        super.onAnimationEnd(animation);
                        animator.view.setVisibility(View.GONE);
                        if (animator.callback != null) animator.callback.onAnimationEnd();
                    }
                });
    }

    private static void animateFade(final Animator animator) {
        final float startAlpha = animator.view.getVisibility() == View.VISIBLE ? 1f : 0f;
        final float endAlpha = animator.view.getVisibility() == View.VISIBLE ? 0f : 1f;

        animator.view.clearAnimation();
        animator.view.setAlpha(startAlpha);
        animator.view.setVisibility(View.VISIBLE);

        animator.view.animate()
                .setDuration(animator.duration)
                .alpha(endAlpha)
                .setInterpolator(animator.interpolator)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(android.animation.Animator animation) {
                        super.onAnimationStart(animation);
                        if (animator.callback != null) animator.callback.onAnimationStart();
                    }

                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        super.onAnimationEnd(animation);
                        if (startAlpha == 1f) {
                            animator.view.setVisibility(View.GONE);
                        }

                        if (animator.callback != null) animator.callback.onAnimationEnd();
                    }
                });
    }

    public static class ViewAnimator {

        private View view;
        private Type type;
        private int duration;
        private Interpolator interpolator;
        private Callback callback;

        private ViewAnimator(@NonNull View view, Type type) {
            this.view = view;
            this.type = type;
            this.duration = 200;
            this.interpolator = new DecelerateInterpolator();
        }

        public ViewAnimator duration(int duration) {
            this.duration = duration;
            return this;
        }

        public ViewAnimator interpolator(@NonNull Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }


        public ViewAnimator callback(@NonNull Callback callback) {
            this.callback = callback;
            return this;
        }

        public void start() {
            switch (type) {
                case SLIDE_DOWN_IN:
                case SLIDE_DOWN_OUT:
                    animateSlideDown(this);
                    break;
                case SLIDE_UP_IN:
                case SLIDE_UP_OUT:
                    animateSlideUp(this);
                    break;
                default:
                    animateSlideDown(this);
                    break;
            }
        }
    }

    private static void animateSlideDown(final ViewAnimator animator) {
        Context context = animator.view.getContext();
        if (context instanceof ContextThemeWrapper) {
            context = ((ContextThemeWrapper) animator.view.getContext()).getBaseContext();
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.android_helpers_animation_slide_down_in);
        if (animator.type == Type.SLIDE_DOWN_OUT) {
            animation = AnimationUtils.loadAnimation(context, R.anim.android_helpers_animation_slide_down_out);
        }

        animation.setInterpolator(animator.interpolator);
        animation.setDuration(animator.duration);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (animator.type == Type.SLIDE_DOWN_IN) {
                    animator.view.setVisibility(View.GONE);
                } else {
                    animator.view.setVisibility(View.VISIBLE);
                }

                if (animator.callback != null) animator.callback.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animator.type == Type.SLIDE_DOWN_IN) {
                    animator.view.setVisibility(View.VISIBLE);
                } else {
                    animator.view.setVisibility(View.GONE);
                }

                if (animator.callback != null) animator.callback.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animator.view.clearAnimation();
        animator.view.startAnimation(animation);
    }

    private static void animateSlideUp(final ViewAnimator animator) {
        Context context = animator.view.getContext();
        if (context instanceof ContextThemeWrapper) {
            context = ((ContextThemeWrapper) animator.view.getContext()).getBaseContext();
        }

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.android_helpers_animation_slide_up_in);
        if (animator.type == Type.SLIDE_UP_OUT) {
            animation = AnimationUtils.loadAnimation(context, R.anim.android_helpers_animation_slide_up_out);
        }

        animation.setInterpolator(animator.interpolator);
        animation.setDuration(animator.duration);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                if (animator.type == Type.SLIDE_UP_IN) {
                    animator.view.setVisibility(View.GONE);
                } else {
                    animator.view.setVisibility(View.VISIBLE);
                }

                if (animator.callback != null) animator.callback.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animator.type == Type.SLIDE_UP_IN) {
                    animator.view.setVisibility(View.VISIBLE);
                } else {
                    animator.view.setVisibility(View.GONE);
                }

                if (animator.callback != null) animator.callback.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animator.view.clearAnimation();
        animator.view.startAnimation(animation);
    }

    private enum Type {
        SHOW,
        HIDE,
        FADE,
        SLIDE_DOWN_IN,
        SLIDE_DOWN_OUT,
        SLIDE_UP_IN,
        SLIDE_UP_OUT
    }

    public interface Callback {
        void onAnimationStart();
        void onAnimationEnd();
    }
}

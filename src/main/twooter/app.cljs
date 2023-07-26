(ns twooter.app
  (:require [twooter.events]
            [twooter.subs]
            [twooter.widgets :refer [button twoot FontAwesomeIcon]]
            [expo.root :as expo-root]
            ["expo-status-bar" :refer [StatusBar]]
            [re-frame.core :as rf]
            ["react-native" :as rn]
            [reagent.core :as r]
            ["native-base" :refer [NativeBaseProvider Avatar]]
            ["@react-navigation/bottom-tabs" :as rnbt]
            ["@react-navigation/native" :as rnn]
            ["@react-navigation/native-stack" :as rnn-stack]))

(defonce shadow-splash (js/require "../assets/shadow-cljs.png"))
(defonce cljs-splash (js/require "../assets/cljs.png"))

(defonce Tab (rnbt/createBottomTabNavigator))
(defonce Stack (rnn-stack/createNativeStackNavigator))

(defn navigate-to [props screen]
  (-> props .-navigation (.navigate screen)))

(defn home [^js props]
  (r/with-let [counter (rf/subscribe [:get-counter])
               tap-enabled? (rf/subscribe [:counter-tappable?])]
    [:> rn/View {:style {:flex 1
                         :padding-vertical 50
                         :justify-content :space-between
                         :align-items :center
                         :background-color :white}}
     [:> rn/View {:style {:align-items :center}}
      [:> rn/Text {:style {:font-weight   :bold
                           :font-size     72
                           :color         :blue
                           :margin-bottom 20}} @counter]
      [button {:on-press #(rf/dispatch [:inc-counter])
               :disabled? (not @tap-enabled?)
               :style {:background-color :blue}}
       "Tap me, I'll count"]]
     [:> rn/View {:style {:align-items :center}}
      [button {:on-press (fn []
                           (-> props .-navigation (.navigate "About")))}
       "Tap me, I'll navigate"]]
     [:> rn/View
      [:> rn/View {:style {:flex-direction :row
                           :align-items :center
                           :margin-bottom 20}}
       [:> rn/Image {:style {:width  160
                             :height 160}
                     :source cljs-splash}]
       [:> rn/Image {:style {:width  160
                             :height 160}
                     :source shadow-splash}]]
      [:> rn/Text {:style {:font-weight :normal
                           :font-size   15
                           :color       :blue}}
       "Using: shadow-cljs+expo+reagent+re-frame"]]
     [:> StatusBar {:style "auto"}]]))

(defn feed-page [^js props]
  [:> rn/View {:style {:flex 1
                       :padding-vertical 50
                       :padding-horizontal 20
                       :justify-content :space-between
                       :align-items :flex-start
                       :background-color :white}}
   [:> rn/View {:style {:align-items :flex-start}}
    [twoot {:author (str "@" "hello")
            :author-display "Amy"
            :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
            :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}]
    [twoot {:author (str "@" "hello")
            :author-display "Amy"
            :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
            :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}]]
   [:> StatusBar {:style "auto"}]])

(defn- about 
  []
  (r/with-let [counter (rf/subscribe [:get-counter])]
    [:> rn/View {:style {:flex 1
                         :padding-vertical 50
                         :padding-horizontal 20
                         :justify-content :space-between
                         :align-items :flex-start
                         :background-color :white}}
     [:> rn/View {:style {:align-items :flex-start}}
      [:> rn/Text {:style {:font-weight   :bold
                           :font-size     54
                           :color         :blue
                           :margin-bottom 20}}
       "About Twooter.App"]
      [:> rn/Text {:style {:font-weight   :bold
                           :font-size     20
                           :color         :blue
                           :margin-bottom 20}}
       (str "Counters is at: " @counter)]
      [:> rn/Text {:style {:font-weight :normal
                           :font-size   15
                           :color       :blue}}
       "Built with React Native, Expo, Reagent, re-frame, and React Navigation"]]
     [:> StatusBar {:style "auto"}]]))


(defn root []
  ;; The save and restore of the navigation root state is for development time bliss
  (r/with-let [!root-state (rf/subscribe [:navigation/root-state])
               save-root-state! (fn [^js state]
                                  (rf/dispatch [:navigation/set-root-state state]))
               add-listener! (fn [^js navigation-ref]
                               (when navigation-ref
                                 (.addListener navigation-ref "state" save-root-state!)))]
    [:> NativeBaseProvider

     [:> rnn/NavigationContainer {:ref add-listener!
                                  :initialState (when @!root-state (-> @!root-state .-data .-state))}
      [:> Tab.Navigator {:initialRouteName "Feed"
                         :screenOptions {:tabBarActiveTintColor "#e91e63"}}
       [:> Tab.Screen {:name "Feed"
                       :component (fn [props] (r/as-element [feed-page props]))
                       :options {:tabBarShowLabel false
                                 :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "home" :size "xl" :color (.-color props)}))}}]
       [:> Tab.Screen {:name "Search"
                       :component (fn [props] (r/as-element [feed-page props]))
                       :options {:tabBarShowLabel false
                                 :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "search" :size "xl" :color (.-color props)}))}}]
       [:> Tab.Screen {:name "Notifications"
                       :component (fn [props] (r/as-element [feed-page props]))
                       :options {:tabBarShowLabel false
                                 :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "bell" :size "xl" :color (.-color props)}))}}]
       [:> Tab.Screen {:name "Profile"
                       :component (fn [props] (r/as-element [feed-page props]))
                       :options {:tabBarShowLabel false
                                 :tabBarIcon (fn [^js props] (r/as-element [:> Avatar {:source {:uri "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"}
                                                                                      :size "sm"}]))}}]
       ]
      #_[:> Stack.Navigator
         [:> Stack.Screen {:name "Home"
                           :component (fn [props] (r/as-element [home props]))
                           :options {:title "Twooter.App"}}]
         [:> Stack.Screen {:name "About"
                           :component (fn [props] (r/as-element [about props]))
                           :options {:title "About"}}]]]]))

(defn start
  {:dev/after-load true}
  []
  (expo-root/render-root (r/as-element [root])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start))

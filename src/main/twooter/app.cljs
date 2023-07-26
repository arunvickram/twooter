(ns twooter.app
  (:require [twooter.events]
            [twooter.subs]
            [twooter.widgets :refer [button twoot FontAwesomeIcon FontAwesome5Icon flat-list]]
            [expo.root :as expo-root]
            ["expo-status-bar" :refer [StatusBar]]
            [re-frame.core :as rf]
            ["react-native" :as rn]
            [reagent.core :as r]
            ["native-base" :refer [NativeBaseProvider Button Box Avatar Input Fab TextArea] :as nb]
            ["@react-navigation/bottom-tabs" :as rnbt]
            ["@react-navigation/native" :as rnn]
            ["@react-navigation/native-stack" :as rnn-stack]))

(defonce theme
  (nb/extendTheme
   #js{"colors" #js{"primary" #js{"50"  "#F5F4FD"
                                  "100" "#E7E3F9"
                                  "200" "#CAC1F2"
                                  "300" "#ADA0EB"
                                  "400" "#907EE4"
                                  "500" "#735CDD"
                                  "600" "#4B2ED3"
                                  "700" "#3A23A6"
                                  "800" "#2A1978"
                                  "900" "#1A0F49"}}}))

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

(defonce DATA
  [{:id 1
    :author (str "@" "hello")
    :author-display "Amy"
    :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
    :like-count 4523
    :reply-count 97
    :retwoot-count 100
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}
   {:id 2
    :author (str "@" "hello")
    :author-display "Amy"
    :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
    :like-count 4523
    :reply-count 97
    :retwoot-count 100
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}
   {:id 3
    :author (str "@" "hello")
    :author-display "Amy"
    :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
    :like-count 4523
    :reply-count 97
    :retwoot-count 100
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}
   {:id 4
    :author (str "@" "hello")
    :author-display "Amy"
    :author-pfp "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"
    :like-count 4523
    :reply-count 97
    :retwoot-count 100
    :text "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."}])

(defn feed-page [^js props]
  [:> rn/View {:style {:flex 1
                       :padding-horizontal 20
                       :justify-content :space-between
                       :align-items :flex-start
                       :background-color :white}}
   [:> rn/View {:style {:align-items :flex-start}}
    [:> rn/FlatList {:data DATA
                     :keyExtractor (fn [data] (.-id data))
                     :renderItem (fn [twoot-data]
                                   (r/as-element [twoot (:item (js->clj twoot-data {:keywordize-keys true}))]))}]]
   [:> Fab {:renderInPortal false
            :shadow 2
            :size "sm"
            :background-color "primary.500"
            :onPress #(props.navigation.navigate "Compose Twoot")
            :icon (FontAwesome5Icon {:name "feather-alt"
                                     :color "white"
                                     :size "sm"})}]
   [:> StatusBar {:style "auto"}]])

(defn search-page [^js props]
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

(defn SearchBar [props]
  (r/as-element [:> Input {:maxW "250"
                           :InputLeftElement (FontAwesomeIcon {:name "search"})
                           :variant "rounded"
                           :size "lg"
                           :mx "auto"
                           :placeholder "Search Twooter"}]))


(defn draft-tweet []
  [:> Box
   [:> TextArea {:placeholder "Compose your twoot here"
                 :size "lg"
                 :w "100%"
                 :h "200"}]])

(defn MainTabs []
  [:> Tab.Navigator {:screenOptions {:tabBarActiveTintColor "primary.500"}}
   [:> Tab.Screen {:name "Feed"
                   :component (fn [props] (r/as-element [feed-page props]))
                   :options {:tabBarShowLabel false
                             :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "home" :size "xl" :color (.-color props)}))}}]
   [:> Tab.Screen {:name "Search"
                   :component (fn [props] (r/as-element [search-page props]))
                   :options {:tabBarShowLabel false
                             :headerLeft (fn [^js props] (r/as-element [:> Avatar {:source {:uri "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"}
                                                                                  :size "sm"
                                                                                  :ml 5
                                                                                  :mr 2}]))
                             :headerTitle (fn [^js props] (r/as-element [:> Input {:w "250"
                                                                                  :variant "rounded"
                                                                                  :InputLeftElement (FontAwesomeIcon {:name "search" :ml 3})
                                                                                  :size "lg"
                                                                                  :mx "auto"
                                                                                  :placeholder "Search Twooter"}]))
                             :headerRight (fn [^js props] (FontAwesomeIcon {:name "gear" :size "lg" :mr 5}))
                             :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "search" :size "xl" :color (.-color props)}))}}]
   [:> Tab.Screen {:name "Notifications"
                   :component (fn [props] (r/as-element [feed-page props]))
                   :options {:tabBarShowLabel false
                             :tabBarBadge 3
                             :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "bell" :size "xl" :color (.-color props)}))}}]
   [:> Tab.Screen {:name "Profile"
                   :component (fn [props] (r/as-element [feed-page props]))
                   :options {:tabBarShowLabel false
                             :tabBarIcon (fn [^js props] (r/as-element [:> Avatar {:source {:uri "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"}
                                                                                  :size "sm"}]))}}]])

(def MainTabsR (r/reactify-component MainTabs))

(defn CancelButton [^js props]
  [:> Button {:variant "ghost"
              :color "coolGray.50"}
   "Cancel"])

(defn root []
  ;; The save and restore of the navigation root state is for development time bliss
  (r/with-let [!root-state (rf/subscribe [:navigation/root-state])
               save-root-state! (fn [^js state]
                                  (rf/dispatch [:navigation/set-root-state state]))
               add-listener! (fn [^js navigation-ref]
                               (when navigation-ref
                                 (.addListener navigation-ref "state" save-root-state!)))]
    [:> NativeBaseProvider {:theme theme}
     [:> rnn/NavigationContainer {:ref add-listener!
                                  :initialState (when @!root-state (-> @!root-state .-data .-state))}
      [:> Stack.Navigator
       [:> Stack.Group
        [:> Stack.Screen {:name "Tabs"
                          :component MainTabsR
                          :options {:headerShown false}}]]
       [:> Stack.Group {:screenOptions {:presentation "modal"}}
        [:> Stack.Screen {:name "Compose Twoot"
                          :component (fn [^js props] (r/as-element [draft-tweet props]))
                          :options (fn [^js screenProps]
                                     {:headerLeft (fn [props] (r/as-element [CancelButton screenProps]))
                                    :headerRight (fn [props] (r/as-element [:> Button {:color "primary.500" :borderRadius "100%" :px 4} "Twoot"]))})}]]
       ]
      #_[:> Tab.Navigator {:initialRouteName "Feed"
                           :screenOptions {:tabBarActiveTintColor "primary.500"}}
         [:> Tab.Screen {:name "Feed"
                         :component (fn [props] (r/as-element [feed-page props]))
                         :options {:tabBarShowLabel false
                                   :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "home" :size "xl" :color (.-color props)}))}}]
         [:> Tab.Screen {:name "Search"
                         :component (fn [props] (r/as-element [search-page props]))
                         :options {:tabBarShowLabel false
                                   :headerLeft (fn [^js props] (r/as-element [:> Avatar {:source {:uri "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"}
                                                                                        :size "sm"
                                                                                        :ml 5
                                                                                        :mr 2}]))
                                   :headerTitle (fn [^js props] (r/as-element [:> Input {:w "250"
                                                                                        :variant "rounded"
                                                                                        :InputLeftElement (FontAwesomeIcon {:name "search" :ml 3})
                                                                                        :size "lg"
                                                                                        :mx "auto"
                                                                                        :placeholder "Search Twooter"}]))
                                   :headerRight (fn [^js props] (FontAwesomeIcon {:name "gear" :size "lg" :mr 5}))
                                   :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "search" :size "xl" :color (.-color props)}))}}]
         [:> Tab.Screen {:name "Notifications"
                         :component (fn [props] (r/as-element [feed-page props]))
                         :options {:tabBarShowLabel false
                                   :tabBarBadge 3
                                   :tabBarIcon (fn [^js props] (FontAwesomeIcon {:name "bell" :size "xl" :color (.-color props)}))}}]
         [:> Tab.Screen {:name "Profile"
                         :component (fn [props] (r/as-element [feed-page props]))
                         :options {:tabBarShowLabel false
                                   :tabBarIcon (fn [^js props] (r/as-element [:> Avatar {:source {:uri "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"}
                                                                                        :size "sm"}]))}}]
         ]]]))

(defn start
  {:dev/after-load true}
  []
  (expo-root/render-root (r/as-element [root])))

(defn init []
  (rf/dispatch-sync [:initialize-db])
  (start))

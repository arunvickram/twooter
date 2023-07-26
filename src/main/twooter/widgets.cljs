(ns twooter.widgets
  (:require ["react-native" :as rn]
            [reagent.core :as r]
            ["@expo/vector-icons" :refer [Ionicons FontAwesome FontAwesome5]]
            ["native-base" :refer [Avatar Box Text HStack VStack Button Icon]]))

(defn ExpoIcon [props]
  (r/as-element [:> Icon props]))

(defn FontAwesomeIcon [props]
  (r/as-element [:> Icon (merge props {:as FontAwesome})]))

(defn FontAwesome5Icon [props]
  (r/as-element [:> Icon (merge props {:as FontAwesome5})]))

(defn flat-list [props]
  [:> rn/FlatList props #_(merge props {:data (:data props)
                                :renderItem (fn [^js props'] (r/as-element ((:render-item props) (js->clj props'))))
                                :keyExtractor (fn [^js props'] (r/as-element ((:key-extractor props) (js->clj props'))))})])

(defn twoot [{:keys [id
                     text
                     author
                     author-pfp
                     author-display]}]
  [:> Box {:borderColor "coolGray.200"
           :px "6"
           :py "2"
           :borderBottomWidth 2}
   [:> VStack {:space 2}
    [:> HStack {:space 3 :alignItems "center" :mt "2"}
     [:> Avatar {:source {:uri author-pfp}}]
     [:> Text author-display]
     [:> Text {:bold true} author]
     [:> Text "2h"]]
    [:> Text text]
    [:> HStack {:space 3}
     [:> Button {:variant "ghost"
                 :leftIcon (ExpoIcon {:as FontAwesome :name "reply" :size "xs"})}
      "2"]
     [:> Button {:variant "ghost"
                 :leftIcon (ExpoIcon {:as FontAwesome :name "retweet" :size "xs"})}
      "100"]
     [:> Button {:variant "ghost"
                 :leftIcon (ExpoIcon {:as FontAwesome :name "heart" :size "xs"})}
      "5,124"]]]])

(defn button [{:keys [style text-style on-press
                      disabled? disabled-style disabled-text-style]
               :or {on-press #()}} text]
  [:> rn/Pressable {:style (cond-> {:font-weight      :bold
                                    :font-size        18
                                    :padding          6
                                    :background-color :blue
                                    :border-radius    999
                                    :margin-bottom    20}
                             :always (merge style)
                             disabled? (merge {:background-color "#aaaaaa"}
                                              disabled-style))
                    :on-press on-press
                    :disabled disabled?}
   [:> rn/Text {:style (cond-> {:padding-left  12
                                :padding-right 12
                                :font-weight   :bold
                                :font-size     18
                                :color         :white}
                         :always (merge text-style)
                         disabled? (merge {:color :white}
                                          disabled-text-style))}
    text]])
